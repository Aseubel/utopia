package com.aseubel.api.dto.message;

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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025-03-19 22:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryMessageResponse implements Serializable {

    @FieldDesc(name = "message_id")
    private String messageId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "目标用户id")
    private String toUserId;

    @FieldDesc(name = "消息内容")
    private String content;

    @FieldDesc(name = "消息类型 image或text")
    private String type;

    @FieldDesc(name = "时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

}
