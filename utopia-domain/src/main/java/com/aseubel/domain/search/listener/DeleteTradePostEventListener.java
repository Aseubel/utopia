package com.aseubel.domain.search.listener;

import com.aseubel.types.common.Constant;
import com.aseubel.types.event.DeleteDiscussPostEvent;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * @author Aseubel
 * @date 2025-03-11 23:06
 */
@Component
public class DeleteTradePostEventListener implements ApplicationListener<DeleteDiscussPostEvent> {

    @Resource
    private Client meilisearchClient;

    @Override
    public void onApplicationEvent(DeleteDiscussPostEvent event) {
        meilisearchClient.index(Constant.getTradePostSearchIndex(event.getSchoolCode())).deleteDocument(event.getPostId());
    }
}
