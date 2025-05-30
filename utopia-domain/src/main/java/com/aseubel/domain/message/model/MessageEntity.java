package com.aseubel.domain.message.model;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Aseubel
 * @date 2025-03-02 11:15
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntity {

    @FieldDesc(name = "消息id")
    private String messageId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "接收者id")
    private String toUserId;

    @FieldDesc(name = "消息内容")
    private String content;

    @FieldDesc(name = "消息类型")
    private String type;

    @FieldDesc(name = "发送时间")
    private LocalDateTime sendTime;

    @FieldDesc(name = "是否已读")
    private Integer status;

    public MessageEntity(String userId, String toUserId, String content, String type) {
        this.userId = userId;
        this.toUserId = toUserId;
        this.content = content;
        this.type = type;
    }

    public String generateMessageId() {
        this.messageId = UUID.randomUUID().toString().replace("-", "");
        return this.messageId;
    }

}
