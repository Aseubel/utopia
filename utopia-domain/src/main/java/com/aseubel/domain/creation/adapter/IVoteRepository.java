package com.aseubel.domain.creation.adapter;

import com.aseubel.domain.creation.model.entity.VoteEntity;

/**
 * @author Aseubel
 * @date 2025/4/8 下午12:06
 */
public interface IVoteRepository {

    void insertVote(VoteEntity vote);
}
