package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.creation.model.entity.TopicEntity;
import com.aseubel.infrastructure.dao.po.Topic;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:43
 */
public class TopicConvertor {

    public static Topic convert(TopicEntity topic) {
        return Topic.builder()
                .topicId(topic.getTopicId())
                .topicName(topic.getTopicName())
                .topicDesc(topic.getTopicDesc())
                .phrase(topic.getPhrase())
                .type(topic.getType())
                .example(topic.getExample())
                .status(topic.getStatus())
                .build();
    }

    public static TopicEntity convert(Topic topic) {
        return TopicEntity.builder()
                .topicId(topic.getTopicId())
                .topicName(topic.getTopicName())
                .topicDesc(topic.getTopicDesc())
                .phrase(topic.getPhrase())
                .type(topic.getType())
                .example(topic.getExample())
                .status(topic.getStatus())
                .build();
    }

    public static <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }
}
