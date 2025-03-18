package com.aseubel.domain.community.model.entity;

import com.aseubel.types.annotation.FieldDesc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Aseubel
 * @description 社区通知类
 * @date 2025-03-18 20:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeEntity {

    @FieldDesc(name = "通知id")
    private String noticeId;

    @FieldDesc(name = "类型 0-评论帖子;1-评论回复")
    private Integer type;

    @FieldDesc(name = "是否已读 0-未读;1-已读")
    private Integer status;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "用户名称")
    private String userName;

    @FieldDesc(name = "用户头像")
    private String userAvatar;

    @FieldDesc(name = "接收者id")
    private String receiverId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "时间")
    private LocalDateTime time;

    @FieldDesc(name = "评论id")
    private String commentId;

    @FieldDesc(name = "回复内容")
    private String commentContent;

    @FieldDesc(name = "我的回复id")
    private String myCommentId;

    @FieldDesc(name = "我的回复评论内容")
    private String myCommentContent;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "帖子标题")
    private String postTitle;

    @FieldDesc(name = "发帖人昵称")
    private String postUserName;

    @FieldDesc(name = "帖子图片")
    private String postImage;

    @FieldDesc(name = "院校代码")
    private String schoolCode;

    public String generateId() {
        this.noticeId = UUID.randomUUID().toString().replaceAll("-", "");
        return this.noticeId;
    }

}
