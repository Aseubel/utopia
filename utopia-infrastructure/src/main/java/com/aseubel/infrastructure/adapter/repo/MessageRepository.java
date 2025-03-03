package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.message.adapter.repo.IMessageRepository;
import com.aseubel.domain.message.model.MessageEntity;
import com.aseubel.infrastructure.convertor.MessageConvertor;
import com.aseubel.infrastructure.dao.MessageMapper;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;

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
        messageMapper.saveMessage(MessageConvertor.convert(message));
    }
}
