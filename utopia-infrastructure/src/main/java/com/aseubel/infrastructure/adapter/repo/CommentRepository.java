package com.aseubel.infrastructure.adapter.repo;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.infrastructure.convertor.CommentConvertor;
import com.aseubel.infrastructure.convertor.CommunityImageConvertor;
import com.aseubel.infrastructure.dao.CommentMapper;
import com.aseubel.infrastructure.dao.ImageMapper;
import com.aseubel.infrastructure.dao.LikeMapper;
import com.aseubel.infrastructure.dao.po.Comment;
import com.aseubel.infrastructure.dao.po.Image;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.util.AliOSSUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RScript;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<CommentEntity> listPostComment(CommunityBO communityBO) {
        String postId = communityBO.getPostId();
        String commentId = communityBO.getCommentId();
        Integer limit = communityBO.getLimit();
        Integer sortType = Optional.ofNullable(communityBO.getSortType()).orElse(0);
        List<String> commentIds = null;

        commentIds = switch (sortType) {
            case 0 ->
                    (List<String>) redisService.getFromSortedSet(RedisKeyBuilder.commentTimeScoreKey(postId), commentId, limit);
            case 1 ->
                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.commentTimeScoreKey(postId), commentId, limit);
            case 2 ->
                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.commentLikeScoreKey(postId), commentId, limit);
            default -> commentIds;
        };
        List<CommentEntity> comments = new ArrayList<>();
        if (commentIds != null) {
            for (String id : commentIds) {
                comments.add(redisService.getValue(RedisKeyBuilder.commentKey(id)));
            }
        }
        // 评论本体
        comments = CollectionUtil.isNotEmpty(comments) ? comments :
                Optional.ofNullable(StringUtils.isEmpty(commentId)
                ? commentMapper.listCommentByPostIdAhead(postId, limit, sortType)
                : commentMapper.listCommentByPostId(postId, commentId, limit, sortType))
                .map(c -> c.stream()
                        .map(commentConvertor::convertToEntity)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        if (CollectionUtil.isEmpty(comments)) {
            return comments;
        }
        for (CommentEntity comment : comments) {
            redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment);
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
        for (CommentEntity comment : comments) {
            redisService.addToSortedSet(RedisKeyBuilder.commentTimeScoreKey(postId), comment.getCommentId(),
                    comment.getCommentTime().toEpochSecond(ZoneOffset.UTC) + comment.getCommentTime().getNano() / 1_000_000_000.0);
            redisService.addToSortedSet(RedisKeyBuilder.commentLikeScoreKey(postId), comment.getCommentId(), comment.getLikeCount());
        }
        return comments;
    }

    @Override
    public List<String> listCommentImages(String commentId) {
        return Optional.ofNullable(commentMapper.listImageUrlByCommentId(commentId)).orElse(Collections.emptyList());
    }

    @Override
    public void saveRootComment(CommentEntity comment) {
        commentMapper.addRootComment(commentConvertor.convertToPO(comment));

        redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment);
        redisService.addToSortedSet(RedisKeyBuilder.commentTimeScoreKey(comment.getPostId()), comment.getCommentId(),
                comment.getCommentTime().toEpochSecond(ZoneOffset.UTC) + comment.getCommentTime().getNano() / 1_000_000_000.0);
        redisService.addToSortedSet(RedisKeyBuilder.commentLikeScoreKey(comment.getPostId()), comment.getCommentId(), 0);
    }

    @Override
    public void saveReplyComment(CommentEntity comment) {
        commentMapper.addChildComment(commentConvertor.convertToPO(comment));

        String rootId = comment.getRootId();
        String script = "local key = KEYS[1]\n" +
                "local increment = ARGV[1]\n" +
                "local comment = redis.call('GET', key)\n" +
                "if comment then\n" +
                "    comment = cjson.decode(comment)\n" +
                "    comment.replyCount = comment.replyCount + increment\n" +
                "    redis.call('SET', key, cjson.encode(comment))\n" +
                "    return comment.replyCount\n" +
                "else\n" +
                "    return nil\n" +
                "end";
        String key = RedisKeyBuilder.commentKey(rootId);
        redisService.executeScript(script, RScript.ReturnType.INTEGER, Collections.singletonList(key), 1);

        redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment);
        redisService.addToSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), comment.getCommentId(),
                comment.getCommentTime().toEpochSecond(ZoneOffset.UTC) + comment.getCommentTime().getNano() / 1_000_000_000.0);
        redisService.addToSortedSet(RedisKeyBuilder.subCommentLikeScoreKey(rootId), comment.getCommentId(), 0);
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
        if (isLike) {
            redisService.incrSortedSetScore(RedisKeyBuilder.commentLikeScoreKey(postId==null?rootId:postId), commentId, 1.0);
        } else {
            redisService.decrSortedSetScore(RedisKeyBuilder.commentLikeScoreKey(postId==null?rootId:postId), commentId, 1.0);
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

    @Override
    public List<CommentEntity> listSubComment(CommunityBO communityBO) {
        String rootId = communityBO.getRootId();
        String commentId = communityBO.getCommentId();
        Integer limit = communityBO.getLimit();
        Integer sortType = Optional.ofNullable(communityBO.getSortType()).orElse(0);

        List<String> commentIds = null;
        commentIds = switch (sortType) {
            case 0 ->
                    (List<String>) redisService.getFromSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), commentId, limit);
            case 1 ->
                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), commentId, limit);
            case 2 ->
                    (List<String>) redisService.getReverseFromSortedSet(RedisKeyBuilder.subCommentLikeScoreKey(rootId), commentId, limit);
            default -> commentIds;
        };
        List<CommentEntity> comments = new ArrayList<>();
        if (commentIds != null) {
            for (String id : commentIds) {
                comments.add(redisService.getValue(RedisKeyBuilder.commentKey(id)));
            }
        }

        // 评论本体
        comments = CollectionUtil.isNotEmpty(comments) ? comments :
                Optional.ofNullable(StringUtils.isEmpty(commentId)
                                ? commentMapper.listSubCommentByRootIdAhead(rootId, limit, sortType)
                                : commentMapper.listSubCommentByRootId(rootId, commentId, limit, sortType))
                        .map(c -> c.stream()
                                .map(commentConvertor::convertToEntity)
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList());
        if (CollectionUtil.isEmpty(comments)) {
            return comments;
        }
        for (CommentEntity comment : comments) {
            redisService.setValue(RedisKeyBuilder.commentKey(comment.getCommentId()), comment);
        }
        // 点赞状态
        comments = comments.stream()
                    .peek(d -> d.setIsLike(likeMapper.getLikeStatus(communityBO.getUserId(), d.getCommentId())
                        .orElse(Optional.ofNullable(
                                (Boolean)redisService.getFromMap(RedisKeyBuilder.LikeStatusKey(communityBO.getUserId()), d.getCommentId()))
                                .orElse(false)))
                    )
                .collect(Collectors.toList());
        // 存入缓存
        for (CommentEntity comment : comments) {
            redisService.addToSortedSet(RedisKeyBuilder.subCommentTimeScoreKey(rootId), comment.getCommentId(),
                    comment.getCommentTime().toEpochSecond(ZoneOffset.UTC) + comment.getCommentTime().getNano() / 1_000_000_000.0);
            redisService.addToSortedSet(RedisKeyBuilder.subCommentLikeScoreKey(rootId), comment.getCommentId(), comment.getLikeCount());
        }
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
            String script = "local key = KEYS[1]\n" +
                    "local increment = ARGV[1]\n" +
                    "local comment = redis.call('GET', key)\n" +
                    "if comment then\n" +
                    "    comment = cjson.decode(comment)\n" +
                    "    comment.replyCount = comment.replyCount - increment\n" +
                    "    redis.call('SET', key, cjson.encode(comment))\n" +
                    "    return comment.replyCount\n" +
                    "else\n" +
                    "    return nil\n" +
                    "end";

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

}
