package com.aseubel.domain.creation.service;

import com.aseubel.domain.creation.model.entity.CreationEntity;
import com.aseubel.domain.creation.model.entity.VoteEntity;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:59
 */
public interface ICreationService {

    /**
     * 添加作品
     * @param creation
     */
    void addCreation(CreationEntity creation);

    /**
     * 增加作品票数
     * @param creationId
     */
    void incrVoteCount(String creationId);

    /**
     * 添加投票
     * @param vote
     */
    void insertVote(VoteEntity vote);
}
