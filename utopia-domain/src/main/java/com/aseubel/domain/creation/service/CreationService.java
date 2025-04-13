package com.aseubel.domain.creation.service;

import com.aseubel.domain.creation.adapter.ICreationRepository;
import com.aseubel.domain.creation.adapter.ITopicRepository;
import com.aseubel.domain.creation.adapter.IVoteRepository;
import com.aseubel.domain.creation.model.entity.CreationEntity;
import com.aseubel.domain.creation.model.entity.VoteEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Aseubel
 * @date 2025/4/8 下午12:10
 */
@Service
@Slf4j
public class CreationService implements ICreationService{

    @Resource
    private ICreationRepository creationRepository;

    @Resource
    private IVoteRepository voteRepository;

    @Resource
    private ITopicRepository topicRepository;

    @Override
    public void addCreation(CreationEntity creation) {
        creationRepository.addCreation(creation);
    }

    @Override
    public void incrVoteCount(String creationId) {
        creationRepository.incrVoteCount(creationId);
    }

    @Override
    public void insertVote(VoteEntity vote) {
        voteRepository.insertVote(vote);
    }
}
