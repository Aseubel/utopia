package com.aseubel.domain.creation.adapter;

import com.aseubel.domain.creation.model.entity.TopicEntity;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/4/8 下午12:06
 */
public interface ITopicRepository {

    void addTopics(List<TopicEntity> topics);

    void selectTopic();

    int getPhrase();

    List<TopicEntity> getTopics();

    TopicEntity getSelectedTopic();
}
