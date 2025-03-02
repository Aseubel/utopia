package com.aseubel.domain.message.adapter.repo;

import com.aseubel.domain.message.model.MessageEntity;

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

}
