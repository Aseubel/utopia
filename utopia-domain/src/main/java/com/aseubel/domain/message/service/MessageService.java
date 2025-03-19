package com.aseubel.domain.message.service;

import com.aseubel.domain.message.adapter.repo.IMessageRepository;
import com.aseubel.domain.message.model.MessageBO;
import com.aseubel.domain.message.model.MessageEntity;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aseubel
 * @description 消息服务实现类
 * @date 2025-03-19 21:50
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService implements IMessageService{

    @Resource
    private IMessageRepository messageRepository;

    @Override
    public List<MessageEntity> getMessages(MessageBO messageBO) {
        List<MessageEntity> messages = messageRepository.listMessage(messageBO);
        return messages;
    }

    @Override
    public void readMessage(MessageBO messageBO) {
        messageRepository.readMessage(messageBO);
    }

}
