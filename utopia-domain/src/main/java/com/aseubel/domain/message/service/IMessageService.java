package com.aseubel.domain.message.service;

import com.aseubel.domain.message.model.MessageBO;
import com.aseubel.domain.message.model.MessageEntity;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-03-19 21:51
 */
public interface IMessageService {

    /**
     * 获取两人之间的所有消息
     * @return
     */
    public List<MessageEntity> getMessages(MessageBO messageBO);

    /**
     * 消息已读
     * @param messageBO
     */
    void readMessage(MessageBO messageBO);
}
