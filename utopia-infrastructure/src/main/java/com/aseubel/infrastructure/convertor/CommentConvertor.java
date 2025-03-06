package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.infrastructure.dao.po.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description 评论实体类转换器
 * @date 2025-02-09 12:27
 */
@Component
public class CommentConvertor {

    public CommentEntity convertToEntity(Comment comment) {
        return CommentEntity.builder()
                .rootId(comment.getRootId())
                .replyTo(comment.getReplyTo())
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .unlikeCount(comment.getUnlikeCount())
                .replyCount(comment.getReplyCount())
                .commentTime(comment.getCommentTime())
                .updateTime(comment.getUpdateTime())
                .build();
    }

    public Comment convertToPO(CommentEntity commentEntity) {
        return Comment.builder()
                .rootId(commentEntity.getRootId())
                .replyTo(commentEntity.getReplyTo())
                .commentId(commentEntity.getCommentId())
                .postId(commentEntity.getPostId())
                .userId(commentEntity.getUserId())
                .content(commentEntity.getContent())
                .likeCount(commentEntity.getLikeCount())
                .unlikeCount(commentEntity.getUnlikeCount())
                .replyCount(commentEntity.getReplyCount())
                .commentTime(commentEntity.getCommentTime())
                .updateTime(commentEntity.getUpdateTime())
                .build();
    }

    public Comment convertToPO(Comment comment) {
        return comment;
    }

    public CommentEntity convertToEntity(CommentEntity comment) {
        return comment;
    }

    public <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }

}
