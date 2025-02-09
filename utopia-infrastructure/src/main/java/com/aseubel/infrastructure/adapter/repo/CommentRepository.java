package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.infrastructure.convertor.CommentConvertor;
import com.aseubel.infrastructure.dao.CommentMapper;
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

    @Override
    public List<CommentEntity> listPostMainComment(String postId) {
        return commentConvertor.convert(
                commentMapper.listTop3CommentsByPostId(postId), commentConvertor::convert);
    }

}
