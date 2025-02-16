package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.infrastructure.convertor.CommentConvertor;
import com.aseubel.infrastructure.convertor.CommunityImageConvertor;
import com.aseubel.infrastructure.dao.CommentMapper;
import com.aseubel.infrastructure.dao.ImageMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

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
    private CommunityImageConvertor communityImageConvertor;

    @Override
    public List<CommentEntity> listPostMainComment(String postId) {
        return commentConvertor.convert(
                commentMapper.listTop3CommentsByPostId(postId), commentConvertor::convert);
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
