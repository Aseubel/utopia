package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Notice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NoticeMapper {

    /**
     * 根据用户id查询通知列表
     * @param userId
     * @return
     */
    @Select("SELECT * FROM `notice` WHERE `receiver_id` = #{userId}")
    List<Notice> listNoticesByUserId(String userId);

    /**
     * 分页查询通知
     * @param userId
     * @param noticeId
     * @param limit
     * @return
     */
    List<Notice> listNotices(String userId, String noticeId, Integer limit);

    /**
     * 插入通知，评论帖子
     * @param notice
     */
    @Insert("INSERT INTO `notice` (`notice_id`, `user_id`, `receiver_id`, `post_id`, `comment_id`, `type`) " +
            "VALUES (#{noticeId}, #{userId}, #{receiverId}, #{postId}, #{commentId}, #{type})")
    void InsertNoticeComment(Notice notice);

    /**
     * 插入通知，评论回复
     * @param notice
     */
    @Insert("INSERT INTO `notice` (`notice_id`, `user_id`, `receiver_id`, `post_id`, `comment_id`, `my_comment_id`, `type`) " +
            "VALUES (#{noticeId}, #{userId}, #{receiverId}, #{postId}, #{commentId}, #{myCommentId}, #{type})")
    void insertNoticeReply(Notice notice);

    /**
     * 删除通知
     * @param noticeId
     */
    @Update("UPDATE `notice` set is_deleted = 1 WHERE notice_id = #{noticeId}")
    void deleteNotice(String noticeId);

}
