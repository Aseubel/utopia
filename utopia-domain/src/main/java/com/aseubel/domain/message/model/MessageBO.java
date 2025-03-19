package com.aseubel.domain.message.model;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025-03-19 22:22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageBO {

    @FieldDesc(name = "消息id")
    private String messageId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "另一用户id")
    private String otherId;

    @FieldDesc(name = "接收者id")
    private String toUserId;

    @FieldDesc(name = "消息内容")
    private String content;

    @FieldDesc(name = "消息类型")
    private String type;

    @FieldDesc(name = "发送时间")
    private LocalDateTime time;

    @FieldDesc(name = "数量")
    private Integer limit;

}
