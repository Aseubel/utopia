package com.aseubel.infrastructure.dao.bazaar;

import com.aseubel.infrastructure.dao.po.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-03-02 12:05
 */
@Mapper
public interface MessageMapper {

    /**
     * 保存消息
     * @param message
     */
    @Insert("insert into `message` (message_id, user_id, to_user_id, content, type) values (#{messageId}, #{userId}, #{toUserId}, #{content}, #{type})")
    void saveMessage(Message message);

    /**
     * 获取消息列表
     * @param userId
     * @param otherId
     * @param messageId
     * @param limit
     * @return
     */
    List<Message> listMessage(String userId, String otherId, String messageId, Integer limit);

    /**
     * 更新消息状态为已读
     * @param userId
     * @param otherId
     * @param messageId
     */
    void readMessage(String userId, String otherId, String messageId);
}
