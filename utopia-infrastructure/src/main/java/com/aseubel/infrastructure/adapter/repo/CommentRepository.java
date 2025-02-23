package com.aseubel.infrastructure.adapter.repo;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
                                commentConvertor.convert(commentMapper.listCommentsByRootId(d.getCommentId()), commentConvertor::convert)))
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

}
