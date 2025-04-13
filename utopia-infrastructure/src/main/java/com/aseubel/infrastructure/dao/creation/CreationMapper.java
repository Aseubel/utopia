package com.aseubel.infrastructure.dao.creation;

import com.aseubel.infrastructure.dao.po.Creation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-04-08 10:58
 */
@Mapper
public interface CreationMapper {

    @Insert("INSERT INTO `creation` (creation_id, topic_id, content) VALUES (#{creationId}, #{topicId}, #{content})")
    void insert(Creation creation);

    @Update("UPDATE `creation` SET voteCount = voteCount + 1 WHERE creation_id = #{creationId}")
    void incrVoteCount(String creationId);

    /**
     * 分页查询话题下的作品
     * @param topicId 话题id
     * @return 作品
     */
    List<Creation> getCreationsByTopicId(String topicId, int limit, Integer lastId, Integer sortType);

    /**
     * 获取用户的作品
     * @param userId 用户id
     * @return 作品
     */
    @Select("SELECT creation_id`, `topic_id`, `rank`, `content`, `vote_count`, `create_time` FROM `creation` WHERE creation_id = #{creationId} ORDER BY id DESC")
    List<Creation> getMyCreation(String userId);

    /**
     * 根据话题id查询作品数量
     * @param topicId 话题id
     * @return 作品数量
     */
    @Select("SELECT COUNT(*) FROM `creation` WHERE topic_id = #{topicId}")
    int countByTopicId(String topicId);

    /**
     * 获取用户某话题发布的作品
     * @param topicId 话题id
     * @param userId 用户id
     * @return 作品
     */
    @Select("SELECT * FROM `creation` WHERE topic_id = #{topicId} AND user_id = #{userId}")
    Creation getThisCreationByUserId(String topicId, String userId);
}
