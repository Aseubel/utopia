package com.aseubel.infrastructure.adapter.repo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.model.vo.Score;
import com.aseubel.domain.search.adapter.repo.ISearchDiscussPostRepository;
import com.aseubel.infrastructure.convertor.CommunityImageConvertor;
import com.aseubel.infrastructure.convertor.DiscussPostConvertor;
import com.aseubel.infrastructure.dao.*;
import com.aseubel.infrastructure.dao.community.DiscussPostMapper;
import com.aseubel.infrastructure.dao.community.FavoriteMapper;
import com.aseubel.infrastructure.dao.community.LikeMapper;
import com.aseubel.infrastructure.dao.po.DiscussPost;
import com.aseubel.infrastructure.dao.po.Image;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import com.aseubel.types.util.UserBasedCollaborativeFiltering;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.aseubel.types.common.Constant.*;

/**
 * @author Aseubel
 * @description 讨论帖子仓储层实现类
 * @date 2025-01-22 13:35
 */
@Repository
@EnableAsync
public class DiscussPostRepository implements IDiscussPostRepository, ISearchDiscussPostRepository {

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private DiscussPostConvertor discussPostConvertor;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private CommunityImageConvertor communityImageConvertor;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private AliOSSUtil aliOSSUtil;

    @Resource
    private IRedisService redisService;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private UserBasedCollaborativeFiltering commender;

    @Override
    public List<DiscussPostEntity> listDiscussPost(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
        String schoolCode = communityBO.getSchoolCode();
        String tag = communityBO.getTag();
        Integer type = communityBO.getType();
        LocalDateTime lastUpdateTime = communityBO.getUpdateTime();

        List<DiscussPostEntity> posts = Optional.ofNullable(StringUtils.isEmpty(postId)
                        ? discussPostMapper.listDiscussPostAhead(limit, schoolCode, tag, type)
                        : discussPostMapper.listDiscussPost(postId, limit, schoolCode, tag, type, lastUpdateTime))
                .map(p -> p.stream()
                        .map(discussPostConvertor::convert)
                        .peek(d -> d.setIsFavorite(favoriteMapper.getFavoriteStatus(userId, d.getPostId()).orElse(false)))
                        .peek(d -> d.setIsLike(likeMapper.getLikeStatus(userId, d.getPostId()).orElse(false)))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        if(CollectionUtil.isNotEmpty(posts)) {
            updateCacheAsync(posts);
        }
        return posts;
    }

    @Async
    public void updateCacheAsync(List<DiscussPostEntity> posts) {
        for (DiscussPostEntity post : posts) {
            redisService.setValue(RedisKeyBuilder.postSchoolCodeKey(post.getPostId()), post.getSchoolCode(), RECENT_POST_CACHE_EXPIRE_TIME);
            redisService.addToMap(RedisKeyBuilder.discussPostCommentCountKey(), post.getPostId(), post.getCommentCount());
        }
    }

    @Override
    public DiscussPostEntity getDiscussPost(CommunityBO communityBO) {
        String postId = communityBO.getPostId();
        String userId = communityBO.getUserId();

        try {
            DiscussPostEntity post = discussPostConvertor.convert(discussPostMapper.getDiscussPostByPostId(postId));
            post.setIsFavorite(favoriteMapper.getFavoriteStatus(userId, postId).orElse(false));
            post.setIsLike(likeMapper.getLikeStatus(userId, postId).orElse(false));
            return post;
        } catch (NullPointerException e) {
            throw new AppException("帖子不存在");
        }
    }

    @Override
    public String getUserIdByPostId(String postId) {
        return discussPostMapper.getUserIdByPostId(postId);
    }

    @Override
    public void savePostImage(CommunityImage postImage) {
        imageMapper.addImage(communityImageConvertor.convert(postImage));
    }

    @Override
    public void saveNewDiscussPost(DiscussPostEntity discussPostEntity) {
        discussPostMapper.addDiscussPost(discussPostConvertor.convert(discussPostEntity));
    }

    @Override
    public void relateNewPostImage(String postId, List<CommunityImage> images) {
        discussPostMapper.relateDiscussPostImage(postId, communityImageConvertor.convert(images, communityImageConvertor::convert));
    }

    @Override
    public List<CommunityImage> listPostImagesByImageIds(List<String> imageIds) {
        return communityImageConvertor.convert(
                imageMapper.listImageByImageIds(imageIds), communityImageConvertor::convert);
    }

    @Override
    public String getPostFirstImage(String postId) {
        // 先获取第一张图片的id。再去image表中查询图片的url
        return Optional.ofNullable(discussPostMapper.getPostFirstImage(postId)).map(imageMapper::getImageUrl).orElse(null);
    }

    @Override
    public List<String> listPostImages(String postId) {
        return Optional.ofNullable(discussPostMapper.listImageUrlByPostId(postId)).orElse(Collections.emptyList());
    }

    @Override
    public String querySchoolName(String schoolCode) {
        return Optional.ofNullable(schoolMapper.getSchoolNameBySchoolCode(schoolCode)).orElse("");
    }

    @Override
    public boolean isSchoolCodeValid(String schoolCode) {
        return Optional.ofNullable(schoolCode).map(schoolMapper::getSchoolBySchoolCode).isPresent();
    }

    @Override
    public List<DiscussPostEntity> queryUserFavoritePosts(CommunityBO bo) {
        List<String> postIds = Optional.ofNullable(StringUtils.isEmpty(bo.getPostId())
                        ? favoriteMapper.listUserFavoritePostIdAhead(bo.getUserId(), bo.getLimit())
                        : favoriteMapper.listUserFavoritePostId(bo.getUserId(), bo.getPostId(), bo.getLimit()))
                .orElse(Collections.emptyList());
        return listSimpleDiscussPost(postIds);
    }

    @Override
    public List<DiscussPostEntity> queryUserDiscussPosts(CommunityBO bo) {
        List<String> postIds = Optional.ofNullable(StringUtils.isEmpty(bo.getPostId())
                        ? discussPostMapper.listUserDiscussPostIdAhead(bo.getUserId(), bo.getLimit())
                        : discussPostMapper.listUserDiscussPostId(bo.getUserId(), bo.getPostId(), bo.getLimit()))
                .orElse(Collections.emptyList());
        return listSimpleDiscussPost(postIds);
    }

    @NotNull
    private List<DiscussPostEntity> listSimpleDiscussPost(List<String> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(discussPostMapper.listDiscussPostByPostIds(postIds))
                .map(l -> {
                    Map<String, DiscussPostEntity> postMap = l.stream()
                            .map(discussPostConvertor::convert) // 先转换为 UserEntity
                            .collect(Collectors.toMap(DiscussPostEntity::getPostId, post -> post));
                    return postIds.stream()
                            .map(postMap::get)
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean favoritePost(String userId, String postId) {
        // 如果jdk升级到9以上可以使用Optional的ifPresentOrElse方法
        boolean isExist = favoriteMapper.getFavoritePostIdByUserIdAndPostId(userId, postId) != null;
        // 新状态
        boolean isFavorite = true;
        if (isExist) {
            isFavorite = !(favoriteMapper.getFavoriteStatus(userId, postId).orElse(false));
            favoriteMapper.updateFavoriteStatus(userId, postId, isFavorite ? 1 : 0);
        } else {
            favoriteMapper.saveFavoriteRecord(userId, postId);
        }

        redisService.addToMap(RedisKeyBuilder.FavoriteStatusKey(userId), postId, isFavorite);
        if (isFavorite) {
            redisService.incrSortedSetScore(RedisKeyBuilder.postFavoriteScoreKey(), postId, 1);
        } else {
            redisService.decrSortedSetScore(RedisKeyBuilder.postFavoriteScoreKey(), postId, 1);
        }
        return isFavorite; // 返回新状态
    }

    @Override
    public boolean getPostFavoriteStatus(String userId, String postId) {
        return favoriteMapper.getFavoriteStatus(userId, postId).orElse(false);
    }

    @Override
    public boolean likePost(String userId, String postId, LocalDateTime likeTime) {
        // 如果jdk升级到9以上可以使用Optional的ifPresentOrElse方法
        boolean isExist = likeMapper.getLikePostIdByUserIdAndToId(userId, postId) != null;
        // 新状态
        boolean isLike = true;
        if (isExist) {
            isLike = !(likeMapper.getLikeStatus(userId, postId).orElse(false));
            likeMapper.updateLikeStatus(userId, postId, likeTime, isLike ? 1 : 0);
        } else {
            likeMapper.saveLikeRecord(userId, postId, likeTime);
        }

        redisService.addToMap(RedisKeyBuilder.LikeStatusKey(userId), postId, isLike);
        if (isLike) {
            redisService.incrSortedSetScore(RedisKeyBuilder.postLikeScoreKey(), postId, 1);
        } else {
            redisService.decrSortedSetScore(RedisKeyBuilder.postLikeScoreKey(), postId, 1);
        }
        return isLike; // 返回新状态
    }

    @Override
    public boolean getPostLikeStatus(String userId, String postId) {
        return likeMapper.getLikeStatus(userId, postId).orElse(false);
    }

    @Override
    public void topPost(String userId, String postId) {
        discussPostMapper.topPost(userId, postId);
    }

    @Override
    public void increaseFavoriteCount(String postId) {
        discussPostMapper.increaseFavoriteCount(postId);
    }

    @Override
    public void increaseLikeCount(String postId) {
        discussPostMapper.increaseLikeCount(postId);
    }

    @Override
    public void increaseCommentCount(String postId) {
        discussPostMapper.increaseCommentCount(postId);

    }

    @Override
    public void decreaseFavoriteCount(String postId) {
        discussPostMapper.decreaseFavoriteCount(postId);
    }

    @Override
    public void decreaseLikeCount(String postId) {
        discussPostMapper.decreaseLikeCount(postId);
    }

    @Override
    public void decreaseCommentCount(String postId) {
        discussPostMapper.decreaseCommentCount(postId);
    }

    @Override
    public void countAll() {
        return;
    }

    @Override
    public void deleteMissingImage() throws ClientException {
        List<Image> imageRecords = imageMapper.listAllPostImageIdAndUrl();
        Set<String> ossRecordSet = new HashSet<>(aliOSSUtil.listObjects());
        List<Long> missingImageIds = new ArrayList<>();
        // 找出数据库中存在但oss没有对应对象的记录
        for (Image image : imageRecords) {
            if (!ossRecordSet.contains(aliOSSUtil.getFileName(image.getImageUrl()))) {
                missingImageIds.add(image.getId());
            }
        }
        if (!CollectionUtil.isEmpty(missingImageIds)) {
            imageMapper.deleteMissingImage(missingImageIds);
        }
    }

    @Override
    public void deletePost(String postId) {
        discussPostMapper.deleteDiscussPostByPostId(postId);
        favoriteMapper.deletePostFavorite(postId);
    }

    @Override
    public boolean isPoster(String userId, String postId) {
        String posterId = discussPostMapper.getUserIdByPostId(postId);
        return posterId.equals(userId);
    }



    @Override
    public String listDiscussPostStatistics() throws JsonProcessingException {
        List<DiscussPost> discussPosts = discussPostMapper.listPostBase();
        List<DiscussPostEntity> discussPostEntities = discussPosts.stream()
               .map(discussPostConvertor::convert)
               .toList();
        for (DiscussPostEntity discussPost : discussPostEntities) {
            List<String> images = discussPostMapper.listImageUrlByPostId(discussPost.getPostId());
            if (CollectionUtil.isNotEmpty(images)) {
                discussPost.setImage(images.get(0));
            } else {
                discussPost.setImage("");
            }
        }
        return objectMapper.writeValueAsString(discussPostEntities);
    }

    @Override
    public String listDiscussPostStatistics(Long postId, int pageSize) throws JsonProcessingException {
        List<DiscussPost> discussPosts = discussPostMapper.listPartialPostBase(postId, pageSize);
        List<DiscussPostEntity> discussPostEntities = discussPosts.stream()
                .map(discussPostConvertor::convert)
                .toList();
        for (DiscussPostEntity discussPost : discussPostEntities) {
            List<String> images = discussPostMapper.listImageUrlByPostId(discussPost.getPostId());
            if (CollectionUtil.isNotEmpty(images)) {
                discussPost.setImage(images.get(0));
            } else {
                discussPost.setImage("");
            }
        }
        return objectMapper.writeValueAsString(discussPostEntities);
    }

    @Override
    public Map<String, String> listRecentPost() {
        return Map.of();
    }

    /**
     * 获取用户对帖子的点赞、收藏、评论数
     */
    private List<Score> listUserPostScore(CommunityBO communityBO) {
//        List<Score> scores = StringUtils.isEmpty(userId)
//                ? discussPostMapper.listUserPostScore()
//                : discussPostMapper.listUserPostScoreByUserId(userId);
        return discussPostMapper.listUserPostScore(communityBO.getSchoolCode());
    }

    @Override
    public List<String> listCommendPostId(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        Map<String, Map<String, Double>> cesScores = new HashMap<>();
//        Map<String, Double> userRating = redisService.getMapToJavaMap(RedisKeyBuilder.userPostScoreKey(userId));
//        if (!ObjectUtil.isEmpty(userRating)) {
//            Set<String> users = redisService.getSetMembers(RedisKeyBuilder.userHasBehaviorInCommunityKey());
//            for (String user : users) {
//                userRating = redisService.getMapToJavaMap(RedisKeyBuilder.userPostScoreKey(user));
//                if (!ObjectUtil.isEmpty(userRating)) {
//                    cesScores.put(user, userRating);
//                }
//            }
//            commender.setUserRatings(cesScores);
//            return commender.generateRecommendations(userId, COMMEND_POST_NEIGHBOR_SIZE, COMMEND_POST_SIZE);
//        }
        List<Score> scores = listUserPostScore(communityBO);
        Map<String, Map<String, int[]>> userBehaviors = scores.stream()
                .collect(Collectors.groupingBy(
                        Score::getUserId, // 按 userId 分组
                        Collectors.toMap(
                                Score::getPostId, // 内层 Map 的键是 postId
                                score -> new int[] {
                                        score.getLikeCount(),
                                        score.getFavoriteCount(),
                                        score.getCommentCount()
                                },
                                // 处理重复的 postId（默认覆盖旧值）
                                (existing, replacement) -> replacement
                        )
                ));
        commender.setUserBehaviors(userBehaviors);
        cesScores = commender.calculateCEScores();
        commender.setUserRatings(cesScores);
//        // 缓存 ces 得分
//        for (Map.Entry<String, Map<String, Double>> entry : cesScores.entrySet()) {
//            // 记录有行为的用户
//            redisService.addToSet(RedisKeyBuilder.userHasBehaviorInCommunityKey(), entry.getKey());
//            for (Map.Entry<String, Double> innerEntry : entry.getValue().entrySet()) {
//                redisService.addToMap(RedisKeyBuilder.userPostCesScoreKey(entry.getKey()), innerEntry.getKey(), innerEntry.getValue());
//            }
//        }

        return commender.generateRecommendations(userId, COMMEND_POST_NEIGHBOR_SIZE, communityBO.getLimit());
    }

    @Override
    public List<DiscussPostEntity> listDiscussPost(List<String> postIds) {
        if (CollectionUtil.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        return discussPostConvertor.convert(
                discussPostMapper.listDiscussPostByPostIds(postIds),
                discussPostConvertor::convert);
    }

}
