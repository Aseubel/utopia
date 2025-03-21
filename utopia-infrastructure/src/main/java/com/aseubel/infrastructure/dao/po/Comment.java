package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 评论持久化对象
 * @date 2025-01-22 12:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "根/最顶级评论id")
    private String rootId;

    @FieldDesc(name = "父评论id")
    private String replyTo;

    @FieldDesc(name = "评论id")
    private String commentId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "评论内容")
    private String content;

    @FieldDesc(name = "点赞数")
    private int likeCount;

    @FieldDesc(name = "踩数")
    private int unlikeCount;
    
    @FieldDesc(name = "回复数")
    private int replyCount;

    @FieldDesc(name = "评论时间")
    private LocalDateTime commentTime;

    @FieldDesc(name = "评论状态修改时间")
    private LocalDateTime updateTime;

}
