package com.aseubel.domain.community.model.entity;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Aseubel
 * @description 评论实体类
 * @date 2025-01-22 13:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @FieldDesc(name = "根/最顶级评论id")
    private String rootId;

    @FieldDesc(name = "回复的评论id")
    private String replyTo;

    @FieldDesc(name = "评论id")
    private String commentId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "回复的用户的名称")
    private String replyToName;

    @FieldDesc(name = "评论内容")
    private String content;

    @FieldDesc(name = "点赞数")
    private Integer likeCount;

    @FieldDesc(name = "踩数")
    private Integer unlikeCount;

    @FieldDesc(name = "回复数")
    private Integer replyCount;

    @FieldDesc(name = "评论时间")
    private LocalDateTime commentTime;

    @FieldDesc(name = "评论状态修改时间")
    private LocalDateTime updateTime;

    @FieldDesc(name = "图片id列表")
    private List<String> images;

    @FieldDesc(name = "评论的回复列表")
    private List<CommentEntity> replyList;

    @FieldDesc(name = "是否点赞")
    private Boolean isLike;

    public void generateCommentId() {
        this.commentId = "cmt_" + UUID.randomUUID().toString().replaceAll("-", "");
    }

}
