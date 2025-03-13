package com.aseubel.domain.search.listener;

import com.aseubel.types.event.LikeEvent;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Aseubel
 * @description 点赞事件监听处理
 * @date 2025-03-13 08:23
 */
@Component
@Slf4j
public class LikeEventListener implements ApplicationListener<LikeEvent> {

    @Resource
    private Client meilisearchClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onApplicationEvent(LikeEvent event) {
        log.info("监听到点赞事件");
//        meilisearchClient.index(DISCUSS_POST_SEARCH_INDEX).
    }
}
