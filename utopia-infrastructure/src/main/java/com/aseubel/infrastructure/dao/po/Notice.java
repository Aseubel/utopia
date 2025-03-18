package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 社区通知类
 * @date 2025-03-18 20:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "通知id")
    private String noticeId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "接收者id")
    private String receiverId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "评论id")
    private String commentId;

    @FieldDesc(name = "被回复的评论id")
    private String myCommentId;

    @FieldDesc(name = "类型 0-评论帖子;1-评论回复")
    private Integer type;

    @FieldDesc(name = "是否已读 0-未读;1-已读")
    private Integer status;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

}
