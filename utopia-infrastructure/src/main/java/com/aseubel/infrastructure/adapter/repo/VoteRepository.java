package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.creation.adapter.IVoteRepository;
import com.aseubel.domain.creation.model.entity.VoteEntity;
import com.aseubel.infrastructure.convertor.VoteConvertor;
import com.aseubel.infrastructure.dao.creation.VoteMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:36
 */
@Repository
public class VoteRepository implements IVoteRepository {

    @Resource
    private VoteMapper voteMapper;

    public void insertVote(VoteEntity vote) {
        voteMapper.insertVote(VoteConvertor.convert(vote));
    }
}
