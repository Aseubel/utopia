package com.aseubel.infrastructure.dao.creation;

import com.aseubel.infrastructure.dao.po.Vote;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:01
 */
@Mapper
public interface VoteMapper {

    @Insert("INSERT INTO `vote`(vite_id, user_id, creation_id) VALUES(#{voteId}, #{userId}, #{creationId})")
    void insertVote(Vote vote);

    /**
     * 根据作品id查询该作品的投票总数
     * @param creationId 作品id
     * @return 投票总数
     */
    @Select("SELECT COUNT(*) FROM `vote` WHERE creation_id = #{creationId}")
    int countByCreationId(String creationId);

    /**
     * 根据话题id查询该话题的投票总数
     * @param topicId 话题id
     * @return 投票总数
     */
    @Select("SELECT COUNT(*) FROM `vote` WHERE topic_id = #{topicId}")
    int countByTopicId(String topicId);
}
