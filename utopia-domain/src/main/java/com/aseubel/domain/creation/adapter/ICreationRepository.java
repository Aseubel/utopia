package com.aseubel.domain.creation.adapter;

import com.aseubel.domain.creation.model.entity.CreationEntity;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/4/8 下午12:08
 */
public interface ICreationRepository {

    /**
     * 添加作品
     * @param creation 作品
     */
    void addCreation(CreationEntity creation);

    /**
     * 增加作品票数
     * @param creationId 作品id
     */
    void incrVoteCount(String creationId);

    /**
     * 获取用户发布的作品
     * @param userId 用户id
     * @return 发布的作品
     */
    List<CreationEntity> getUserCreation(String userId);

    /**
     * 获取话题下的作品
     * @param topicId 话题id
     * @return 话题下的作品
     */
    List<CreationEntity> getTopicCreation(String topicId, int limit, Integer lastId, Integer sortType);

    /**
     * 获取排名最高的作品
     * @return 排名最高的作品
     */
    CreationEntity getTopRank(String topicId);

}
