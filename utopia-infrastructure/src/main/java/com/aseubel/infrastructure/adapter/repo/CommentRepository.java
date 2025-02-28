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
import com.aseubel.infrastructure.dao.po.Image;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.util.AliOSSUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
                commentMapper.listTop3CommentsByPostId(postId), commentConvertor::convert);
    }

    @Override
    public List<CommentEntity> listPostComment(CommunityBO communityBO) {
        String postId = communityBO.getPostId();
        String commentId = communityBO.getCommentId();
        Integer limit = communityBO.getLimit();
        Integer sortType = communityBO.getSortType();

        return Optional.ofNullable(StringUtils.isEmpty(commentId)
                        ? commentMapper.listCommentByPostIdAhead(postId, limit, sortType)
                        : commentMapper.listCommentByPostId(postId, commentId, limit, sortType))
                .map(p -> p.stream()
                        .map(commentConvertor::convert)
                        .peek(d -> d.setReplyList(
                                commentConvertor.convert(commentMapper.listTop3CommentsByRootId(d.getCommentId()), commentConvertor::convert)))
                        .peek(d -> d.setIsLike(likeMapper.getLikeStatus(communityBO.getUserId(), d.getCommentId()).orElse(false)))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<String> listCommentImages(String commentId) {
        return Optional.ofNullable(commentMapper.listImageUrlByCommentId(commentId)).orElse(Collections.emptyList());
    }

    @Override
    public void saveRootComment(CommentEntity commentEntity) {
        commentMapper.addRootComment(commentConvertor.convert(commentEntity));
    }

    @Override
    public void saveReplyComment(CommentEntity commentEntity) {
        commentMapper.addChildComment(commentConvertor.convert(commentEntity));
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
    public boolean likeComment(String userId, String commentId, LocalDateTime likeTime) {
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
            redisService.incr(RedisKeyBuilder.commentLikeCountKey(commentId));
        } else {
            redisService.decr(RedisKeyBuilder.commentLikeCountKey(commentId));
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
        Integer sortType = communityBO.getSortType();

        return Optional.ofNullable(StringUtils.isEmpty(commentId)
                        ? commentMapper.listSubCommentByRootIdAhead(rootId, limit, sortType)
                        : commentMapper.listSubCommentByRootId(rootId, commentId, limit, sortType))
                .map(p -> p.stream()
                        .map(commentConvertor::convert)
                        .peek(d -> d.setIsLike(likeMapper.getLikeStatus(communityBO.getUserId(), d.getCommentId()).orElse(false)))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
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
    public void deleteComment(String commentId) {
        commentMapper.deleteCommentByCommentId(commentId);
    }

    @Override
    public void deleteSubComment(String commentId) {
        commentMapper.deleteCommentByRootId(commentId);
    }

}
