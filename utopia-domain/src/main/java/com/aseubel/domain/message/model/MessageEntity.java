package com.aseubel.domain.message.model;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.time.LocalDateTime;

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

    public MessageEntity(String userId, String toUserId, String content, String type) {
        this.userId = userId;
        this.toUserId = toUserId;
        this.content = content;
        this.type = type;
    }

}
