package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.message.model.MessageEntity;
import com.aseubel.infrastructure.dao.po.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description MessageConvertor
 * @date 2025-03-02 13:46
 */
@Component
public class MessageConvertor {

    public static Message convert(MessageEntity messageEntity) {
        return Message.builder()
                .messageId(messageEntity.getMessageId())
                .userId(messageEntity.getUserId())
                .toUserId(messageEntity.getToUserId())
                .content(messageEntity.getContent())
                .type(messageEntity.getType())
                .createTime(messageEntity.getSendTime())
                .status(messageEntity.getStatus())
                .build();
    }

    public static MessageEntity convert(Message message) {
        return MessageEntity.builder()
                .messageId(message.getMessageId())
                .userId(message.getUserId())
                .toUserId(message.getToUserId())
                .content(message.getContent())
                .type(message.getType())
                .sendTime(message.getCreateTime())
                .status(message.getStatus())
                .build();
    }

    public static <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }

}
