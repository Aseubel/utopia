package com.aseubel.domain.search.listener;

import com.aseubel.types.common.Constant;
import com.aseubel.types.event.DeleteTradePostEvent;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;


/**
 * @author Aseubel
 * @date 2025-03-11 23:06
 */
//@Component
public class DeleteTradePostEventListener implements ApplicationListener<DeleteTradePostEvent> {

    @Resource
    private Client meilisearchClient;

    @Override
    public void onApplicationEvent(DeleteTradePostEvent event) {
        meilisearchClient.index(Constant.getTradePostSearchIndex(event.getSchoolCode())).deleteDocument(event.getPostId());
    }
}
