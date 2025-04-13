package com.aseubel.infrastructure.adapter.repo;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.infrastructure.convertor.CommentConvertor;
import com.aseubel.infrastructure.convertor.CommunityImageConvertor;
import com.aseubel.infrastructure.dao.community.CommentMapper;
import com.aseubel.infrastructure.dao.ImageMapper;
import com.aseubel.infrastructure.dao.community.LikeMapper;
import com.aseubel.infrastructure.dao.po.Image;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.util.AliOSSUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RScript;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.aseubel.types.common.Constant.COMMENT_CACHE_EXPIRE_TIME;

/**
 * @author Aseuebl
 * @description 评论仓储层接口实现类1
 * @date 2025-02-09 12:25
 */
@Repository
public class CommentRepository implements ICommentRepository {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private CommentConvertor commentConvertor;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private CommunityImageConvertor communityImageConvertor;

    @Resource
    private IRedisService redisService;

    @Resource
    private AliOSSUtil aliOSSUtil;

    @Override
    public List<CommentEntity> listPostMainComment(String postId) {
        return commentConvertor.convert(
                commentMapper.listTop3CommentsByPostId(postId), commentConvertor::convertToEntity);
    }

//    @Override
//    public List<CommentEntity> listPostComment(CommunityBO communityBO) {
//        String postId = communityBO.getPostId();
//        String commentId = communityBO.getCommentId();
//        Integer limit = communityBO.getLimit();
//        Integer sortType = Optional.ofNullable(communityBO.getSortType()).orElse(0);
//        int likeCount = communityBO.getLikeCount();
//
//        List<String> commentIds = null;
//        boolean isCache = true;
//
//        commentIds = switch (sortType) {
//            case 0 ->
//                    (List<String>) redisService.getFromSortedSet(RedisKeyBuilder.commentTimeScoreKey(postId), commentId, limit);
//            case 1 ->
//                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.commentTimeScoreKey(postId), commentId, limit);
//            case 2 ->
//                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.commentLikeScoreKey(postId), commentId, limit);
//            default -> commentIds;
//        };
//        List<CommentEntity> comments = new ArrayList<>();
//        if (CollectionUtil.isNotEmpty(commentIds)) {
//            for (String id : commentIds) {
//                CommentEntity comment = redisService.getValue(RedisKeyBuilder.commentKey(id));
//                if (comment==null) {
//                    comments = new ArrayList<>();
//                    break;
//                }
//                comments.add(redisService.getValue(RedisKeyBuilder.commentKey(id)));
//            }
//        }
//        // 评论本体
//        if (CollectionUtil.isEmpty(comments)) {
//            comments = Optional.ofNullable(StringUtils.isEmpty(commentId)
//                            ? commentMapper.listCommentByPostIdAhead(postId, limit, sortType)
//                            : commentMapper.listCommentByPostId(postId, commentId, limit, sortType, likeCount))
//                    .map(c -> c.stream()
//                            .map(commentConvertor::convertToEntity)
//                            .collect(Collectors.toList()))
//                    .orElse(Collections.emptyList());
//            isCache = false;
//        }
//
//        if (CollectionUtil.isEmpty(comments)) {
//            return comments;
//        }
//        for (CommentEntity comment : comments) {
//            redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment, COMMENT_CACHE_EXPIRE_TIME);
//        }
//        // 点赞状态和主要回复
//        comments = comments.stream()
//                .peek(d -> d.setReplyList(
//                        commentConvertor.convert(commentMapper.listTop3CommentsByRootId(d.getCommentId()), commentConvertor::convertToEntity)))
//                .peek(d -> d.setIsLike(likeMapper.getLikeStatus(communityBO.getUserId(), d.getCommentId())
//                        .orElse(Optional.ofNullable(
//                                (Boolean)redisService.getFromMap(RedisKeyBuilder.LikeStatusKey(d.getCommentId()), d.getCommentId()))
//                                .orElse(false)))
//                )
//                .collect(Collectors.toList());
//        if (!isCache) {
//            for (CommentEntity comment : comments) {
//                redisService.addToSortedSet(RedisKeyBuilder.commentTimeScoreKey(postId), comment.getCommentId(), getTimeScore(comment));
//                redisService.addToSortedSet(RedisKeyBuilder.commentLikeScoreKey(postId), comment.getCommentId(), getLikeScore(comment));
//            }
//        }
//        return comments;
//    }

    @Override
    public List<CommentEntity> listPostComment(CommunityBO communityBO) {
        String postId = communityBO.getPostId();
        String commentId = communityBO.getCommentId();
        Integer limit = communityBO.getLimit();
        Integer sortType = Optional.ofNullable(communityBO.getSortType()).orElse(0);
        int likeCount = communityBO.getLikeCount();

        // 评论本体
        List<CommentEntity> comments = Optional.ofNullable(StringUtils.isEmpty(commentId)
                            ? commentMapper.listCommentByPostIdAhead(postId, limit, sortType)
                            : commentMapper.listCommentByPostId(postId, commentId, limit, sortType, likeCount))
                    .map(c -> c.stream()
                            .map(commentConvertor::convertToEntity)
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

        if (CollectionUtil.isEmpty(comments)) {
            return comments;
        }
        // 点赞状态和主要回复
        comments = comments.stream()
                .peek(d -> d.setReplyList(
                        commentConvertor.convert(commentMapper.listTop3CommentsByRootId(d.getCommentId()), commentConvertor::convertToEntity)))
                .peek(d -> d.setIsLike(likeMapper.getLikeStatus(communityBO.getUserId(), d.getCommentId())
                        .orElse(Optional.ofNullable(
                                        (Boolean)redisService.getFromMap(RedisKeyBuilder.LikeStatusKey(d.getCommentId()), d.getCommentId()))
                                .orElse(false)))
                )
                .collect(Collectors.toList());
        return comments;
    }

    @Override
    public List<String> listCommentImages(String commentId) {
        return Optional.ofNullable(commentMapper.listImageUrlByCommentId(commentId)).orElse(Collections.emptyList());
    }

    @Override
    public void saveRootComment(CommentEntity comment) {
        commentMapper.addRootComment(commentConvertor.convertToPO(comment));

        redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment, COMMENT_CACHE_EXPIRE_TIME);
        redisService.addToSortedSet(RedisKeyBuilder.commentTimeScoreKey(comment.getPostId()), comment.getCommentId(), getTimeScore(comment));
        redisService.addToSortedSet(RedisKeyBuilder.commentLikeScoreKey(comment.getPostId()), comment.getCommentId(), getLikeScore(comment));
    }

    @Override
    public void saveReplyComment(CommentEntity comment) {
        commentMapper.addChildComment(commentConvertor.convertToPO(comment));

        String rootId = comment.getRootId();
        String script = """
                local key = KEYS[1]
                local increment = ARGV[1]
                local comment = redis.call('GET', key)
                if comment then
                    comment = cjson.decode(comment)
                    comment.replyCount = comment.replyCount + increment
                    redis.call('SET', key, cjson.encode(comment))
                    return comment.replyCount
                else
                    return nil
                end""";
        String key = RedisKeyBuilder.commentKey(rootId);
        redisService.executeScript(script, RScript.ReturnType.INTEGER, Collections.singletonList(key), 1);

        redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment, COMMENT_CACHE_EXPIRE_TIME);
        redisService.addToSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), comment.getCommentId(), getTimeScore(comment));
        redisService.addToSortedSet(RedisKeyBuilder.subCommentLikeScoreKey(rootId), comment.getCommentId(), getLikeScore(comment));
    }

    @Override
    public void relateNewCommentImage(String commentId, List<CommunityImage> images) {
        commentMapper.relateCommentImage(commentId, communityImageConvertor.convert(images, communityImageConvertor::convert));
    }

    @Override
    public List<CommunityImage> listCommentImagesByImageIds(List<String> imageIds) {
        return communityImageConvertor.convert(
                imageMapper.listImageByImageIds(imageIds), communityImageConvertor::convert);
    }

    @Override
    public void saveCommentImage(CommunityImage postImage) {
        imageMapper.addImage(communityImageConvertor.convert(postImage));
    }

    @Override
    public boolean likeComment(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String commentId = communityBO.getCommentId();
        LocalDateTime likeTime = communityBO.getEventTime();
        String postId = communityBO.getPostId();
        String rootId = communityBO.getRootId();

        // 如果jdk升级到9以上可以使用Optional的ifPresentOrElse方法
        boolean isExist = likeMapper.getLikePostIdByUserIdAndToId(userId, commentId) != null;
        // 新状态
        boolean isLike = true;
        if (isExist) {
            isLike = !(likeMapper.getLikeStatus(userId, commentId).orElse(false));
            likeMapper.updateLikeStatus(userId, commentId, likeTime, isLike ? 1 : 0);
        } else {
            likeMapper.saveLikeRecord(userId, commentId, likeTime);
        }

        redisService.addToMap(RedisKeyBuilder.LikeStatusKey(userId), commentId, isLike);
        String key = RedisKeyBuilder.commentKey(commentId);
        String script = """
                    local key = KEYS[1]
                    local increment = ARGV[1]
                    local comment = redis.call('GET', key)
                    if comment then
                        comment = cjson.decode(comment)
                        comment.likeCount = comment.likeCount + increment
                        redis.call('SET', key, cjson.encode(comment))
                        return comment.likeCount
                    else
                        return nil
                    end""";
        if (isLike) {
            redisService.executeScript(script, RScript.ReturnType.INTEGER, Collections.singletonList(key), 1);
            redisService.incrSortedSetScore(StringUtils.isEmpty(rootId)?RedisKeyBuilder.commentLikeScoreKey(postId):RedisKeyBuilder.subCommentLikeScoreKey(rootId),
                    commentId, likeScoreDelta(1.0));
        } else {
            redisService.executeScript(script, RScript.ReturnType.INTEGER, Collections.singletonList(key), -1);
            redisService.decrSortedSetScore(StringUtils.isEmpty(rootId)?RedisKeyBuilder.commentLikeScoreKey(postId):RedisKeyBuilder.subCommentLikeScoreKey(rootId),
                    commentId, likeScoreDelta(1.0));
        }
        return isLike; // 返回新状态
    }

    @Override
    public void increaseLikeCount(String commentId) {
        commentMapper.increaseLikeCount(commentId);
    }

    @Override
    public void decreaseLikeCount(String commentId) {
        commentMapper.decreaseLikeCount(commentId);
    }

//    @Override
//    public List<CommentEntity> listSubComment(CommunityBO communityBO) {
//        String rootId = communityBO.getRootId();
//        String commentId = communityBO.getCommentId();
//        Integer limit = communityBO.getLimit();
//        int likeCount = communityBO.getLikeCount();
//        Integer sortType = Optional.ofNullable(communityBO.getSortType()).orElse(0);
//
//        List<String> commentIds = null;
//        boolean isCache = true;
//        commentIds = switch (sortType) {
//            case 0 ->
//                    (List<String>) redisService.getFromSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), commentId, limit);
//            case 1 ->
//                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), commentId, limit);
//            case 2 ->
//                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.subCommentLikeScoreKey(rootId), commentId, limit);
//            default -> commentIds;
//        };
//        List<CommentEntity> comments = new ArrayList<>();
//        if (CollectionUtil.isNotEmpty(commentIds)) {
//            for (String id : commentIds) {
//                CommentEntity comment = redisService.getValue(RedisKeyBuilder.commentKey(id));
//                if (comment==null) {
//                    comments = new ArrayList<>();
//                    break;
//                }
//                comments.add(redisService.getValue(RedisKeyBuilder.commentKey(id)));
//            }
//        }
//
//        // 评论本体
//        if (CollectionUtil.isEmpty(comments)) {
//            comments = Optional.ofNullable(StringUtils.isEmpty(commentId)
//                            ? commentMapper.listSubCommentByRootIdAhead(rootId, limit, sortType)
//                            : commentMapper.listSubCommentByRootId(rootId, commentId, limit, sortType, likeCount))
//                    .map(c -> c.stream()
//                            .map(commentConvertor::convertToEntity)
//                            .collect(Collectors.toList()))
//                    .orElse(Collections.emptyList());
//            isCache = false;
//        }
//
//        if (CollectionUtil.isEmpty(comments)) {
//            return comments;
//        }
//        for (CommentEntity comment : comments) {
//            redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment, COMMENT_CACHE_EXPIRE_TIME);
//        }
//        // 点赞状态
//        comments = comments.stream()
//                    .peek(d -> d.setIsLike(likeMapper.getLikeStatus(communityBO.getUserId(), d.getCommentId())
//                        .orElse(Optional.ofNullable(
//                                (Boolean) redisService.getFromMap(RedisKeyBuilder.LikeStatusKey(communityBO.getUserId()), d.getCommentId()))
//                                .orElse(false)))
//                    )
//                .collect(Collectors.toList());
//        // 存入缓存
//        if (!isCache) {
//            for (CommentEntity comment : comments) {
//                redisService.addToSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), comment.getCommentId(), getTimeScore(comment));
//                redisService.addToSortedSet(RedisKeyBuilder.subCommentLikeScoreKey(rootId), comment.getCommentId(), getLikeScore(comment));
//            }
//        }
//        return comments;
//    }

    @Override
    public List<CommentEntity> listSubComment(CommunityBO communityBO) {
        String rootId = communityBO.getRootId();
        String commentId = communityBO.getCommentId();
        Integer limit = communityBO.getLimit();
        int likeCount = communityBO.getLikeCount();
        Integer sortType = Optional.ofNullable(communityBO.getSortType()).orElse(0);

        // 评论本体
        List<CommentEntity> comments = Optional.ofNullable(StringUtils.isEmpty(commentId)
                            ? commentMapper.listSubCommentByRootIdAhead(rootId, limit, sortType)
                            : commentMapper.listSubCommentByRootId(rootId, commentId, limit, sortType, likeCount))
                    .map(c -> c.stream()
                            .map(commentConvertor::convertToEntity)
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

        // 点赞状态
        comments = comments.stream()
                .peek(d -> d.setIsLike(likeMapper.getLikeStatus(communityBO.getUserId(), d.getCommentId())
                        .orElse(Optional.ofNullable(
                                        (Boolean) redisService.getFromMap(RedisKeyBuilder.LikeStatusKey(communityBO.getUserId()), d.getCommentId()))
                                .orElse(false)))
                )
                .collect(Collectors.toList());

        return comments;
    }

    @Override
    public void deleteMissingImage() throws ClientException {
        List<Image> imageRecords = imageMapper.listAllCommentImageIdAndUrl();
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
    public void increaseCommentCount(String commentId) {
        commentMapper.increaseCommentCount(commentId);
    }

    @Override
    public String getUserIdByCommentId(String commentId) {
        return commentMapper.getUserIdByCommentId(commentId);
    }

    @Override
    public void deleteComment(CommunityBO communityBO) {
        commentMapper.deleteCommentByCommentId(communityBO.getCommentId());

        redisService.remove(RedisKeyBuilder.commentKey(communityBO.getCommentId()));
        if (StringUtils.isEmpty(communityBO.getRootId())) {
            redisService.RemoveFromSortedSet(RedisKeyBuilder.commentTimeScoreKey(communityBO.getPostId()), communityBO.getCommentId());
            redisService.RemoveFromSortedSet(RedisKeyBuilder.commentLikeScoreKey(communityBO.getPostId()), communityBO.getCommentId());
        } else {
            String script = """
                    local key = KEYS[1]
                    local increment = ARGV[1]
                    local comment = redis.call('GET', key)
                    if comment then
                        comment = cjson.decode(comment)
                        comment.replyCount = comment.replyCount - increment
                        redis.call('SET', key, cjson.encode(comment))
                        return comment.replyCount
                    else
                        return nil
                    end""";

            String key = RedisKeyBuilder.commentKey(communityBO.getRootId());
            redisService.executeScript(script, RScript.ReturnType.INTEGER, Collections.singletonList(key), 1);
            redisService.RemoveFromSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(communityBO.getRootId()), communityBO.getCommentId());
            redisService.RemoveFromSortedSet(RedisKeyBuilder.subCommentLikeScoreKey(communityBO.getRootId()), communityBO.getCommentId());
        }
    }

    @Override
    public void deleteSubComment(String commentId) {
        commentMapper.deleteCommentByRootId(commentId);
    }

    @Override
    public void decreaseRootCommentReplyCount(String commentId) {
        commentMapper.decreaseReplyCount(commentId);
    }

    @Override
    public boolean isCommenter(String userId, String commentId) {
        String commenterId = commentMapper.getUserIdByCommentId(commentId);
        return userId.equals(commenterId);
    }

    /**
     * 计算评论的热度得分
     * 6位存储点赞数，10位存储评论时间
     */
    private double getLikeScore(CommentEntity comment) {
        return Math.pow(10, 16) + comment.getLikeCount() * Math.pow(10, 10) + getTimeScore(comment);
    }

    private double likeScoreDelta(double delta) {
        return Math.pow(10, 10) * delta;
    }

    private double getTimeScore(CommentEntity comment) {
        LocalDateTime commentTime = comment.getCommentTime();
        return commentTime.toEpochSecond(ZoneOffset.UTC) + commentTime.getNano() / 1_000_000_000.0;
    }

}
