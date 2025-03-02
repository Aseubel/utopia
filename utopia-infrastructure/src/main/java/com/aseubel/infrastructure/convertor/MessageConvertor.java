package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.message.model.MessageEntity;
import com.aseubel.infrastructure.dao.po.Message;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description MessageConvertor
 * @date 2025-03-02 13:46
 */
@Component
public class MessageConvertor {

    public static Message convert(MessageEntity messageEntity) {
        return Message.builder()
                .userId(messageEntity.getUserId())
                .toUserId(messageEntity.getToUserId())
                .content(messageEntity.getContent())
                .type(messageEntity.getType())
                .createTime(messageEntity.getSendTime())
                .build();
    }

    public static MessageEntity convert(Message message) {
        return MessageEntity.builder()
                .userId(message.getUserId())
                .toUserId(message.getToUserId())
                .content(message.getContent())
                .type(message.getType())
                .sendTime(message.getCreateTime())
                .build();
    }

}
