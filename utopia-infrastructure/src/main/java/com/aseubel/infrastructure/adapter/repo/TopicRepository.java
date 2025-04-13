package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.creation.adapter.ITopicRepository;
import com.aseubel.domain.creation.model.entity.TopicEntity;
import com.aseubel.infrastructure.convertor.TopicConvertor;
import com.aseubel.infrastructure.dao.creation.TopicMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:50
 */
@Repository
public class TopicRepository implements ITopicRepository {

    @Resource
    private TopicMapper topicMapper;

    @Override
    public void addTopics(List<TopicEntity> topics) {
        topicMapper.addTopics(TopicConvertor.convert(topics, TopicConvertor::convert));
    }

    @Override
    public void selectTopic() {
        topicMapper.changeToSelected();
    }

    @Override
    public int getPhrase() {
        return Optional.ofNullable(topicMapper.getPhrase()).orElse(1);
    }

    @Override
    public List<TopicEntity> getTopics() {
        return TopicConvertor.convert(topicMapper.getTopics(), TopicConvertor::convert);
    }

    @Override
    public TopicEntity getSelectedTopic() {
        return TopicConvertor.convert(topicMapper.getSelectedTopic());
    }
}
