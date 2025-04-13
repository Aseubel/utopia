package com.aseubel.domain.creation.task;

import com.aseubel.domain.creation.adapter.ITopicRepository;
import com.aseubel.domain.creation.model.entity.TopicEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025/4/8 下午12:14
 */
@Component
@Slf4j
public class TopicTask {

    @Resource
    private ITopicRepository topicRepository;

    /**
     * 每周六 23 点 59 分获取新候选话题
     */
    @Scheduled(cron = "0 59 23 * * 6")
    public void getTopic() {
        List<TopicEntity> topics = Collections.emptyList();
        topicRepository.addTopics(topics);
    }

    /**
     * 每周一 0 时票选当周话题
     */
    @Scheduled(cron = "0 0 0 * * 1")
    public void selectTopic() {
        topicRepository.selectTopic();
    }
}
