package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.creation.adapter.ICreationRepository;
import com.aseubel.domain.creation.model.entity.CreationEntity;
import com.aseubel.infrastructure.convertor.CreationConvertor;
import com.aseubel.infrastructure.dao.creation.CreationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:48
 */
@Repository
public class CreationRepository implements ICreationRepository {

    @Resource
    private CreationMapper creationMapper;

    @Override
    public void addCreation(CreationEntity creation) {
        creationMapper.insert(CreationConvertor.convert(creation));
    }

    @Override
    public void incrVoteCount(String creationId) {
        creationMapper.incrVoteCount(creationId);
    }

    @Override
    public List<CreationEntity> getUserCreation(String userId) {
        return CreationConvertor.convert(creationMapper.getMyCreation(userId), CreationConvertor::convert);
    }

    @Override
    public List<CreationEntity> getTopicCreation(String topicId, int limit, Integer lastId, Integer sortType) {
        return CreationConvertor.convert(creationMapper.getCreationsByTopicId(topicId, limit, lastId, sortType), CreationConvertor::convert);
    }

    @Override
    public CreationEntity getTopRank(String topicId) {
        return null;
    }


}
