package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
    @Insert("insert into `message` (user_id, to_user_id, content, type) values (#{userId}, #{toUserId}, #{content}, #{type})")
    void saveMessage(Message message);

}
