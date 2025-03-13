package com.aseubel.domain.search.listener;

import com.aseubel.types.common.Constant;
import com.aseubel.types.event.CompleteTradeEvent;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @date 2025-03-13 12:27
 */
@Component
public class CompleteTradeEventListener implements ApplicationListener<CompleteTradeEvent> {

    @Resource
    private Client meilisearchClient;

    @Override
    public void onApplicationEvent(CompleteTradeEvent event) {
        meilisearchClient.index(Constant.getTradePostSearchIndex(event.getSchoolCode())).deleteDocument(event.getPostId());
    }
}
