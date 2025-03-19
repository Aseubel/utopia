package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.message.adapter.repo.IMessageRepository;
import com.aseubel.domain.message.model.MessageBO;
import com.aseubel.domain.message.model.MessageEntity;
import com.aseubel.infrastructure.convertor.MessageConvertor;
import com.aseubel.infrastructure.dao.MessageMapper;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * @author Aseubel
 * @description 消息仓储层实现
 * @date 2025-03-02 12:05
 */
@Repository
public class MessageRepository implements IMessageRepository {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public void saveMessage(MessageEntity message) {
        message.generateMessageId();
        messageMapper.saveMessage(MessageConvertor.convert(message));
    }

    @Override
    public List<MessageEntity> listMessage(MessageBO messageBO) {
        String userId = messageBO.getUserId();
        String otherId = messageBO.getOtherId();
        String messageId = messageBO.getMessageId();
        Integer limit = messageBO.getLimit();

        List<MessageEntity> messages = MessageConvertor.convert(messageMapper.listMessage(userId, otherId, messageId, limit), MessageConvertor::convert);
        return messages;
    }

    @Override
    public void readMessage(MessageBO messageBO) {
        String userId = messageBO.getUserId();
        String otherId = messageBO.getOtherId();
        String messageId = messageBO.getMessageId();

        messageMapper.readMessage(userId, otherId, messageId);
    }
}
