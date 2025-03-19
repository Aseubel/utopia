package com.aseubel.domain.message.adapter.repo;

import com.aseubel.domain.message.model.MessageBO;
import com.aseubel.domain.message.model.MessageEntity;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-03-02 11:52
 */
public interface IMessageRepository {

    /**
     * 存储消息
     * @param message
     */
    void saveMessage(MessageEntity message);

    /**
     * 获取消息列表
     * @return 消息列表
     */
    List<MessageEntity> listMessage(MessageBO messageBO);

    /**
     * 消息已读
     * @param messageBO
     */
    void readMessage(MessageBO messageBO);
}
