package com.aseubel.domain.search.listener;

import com.aseubel.types.common.Constant;
import com.aseubel.types.event.PublishDiscussPostEvent;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * @author Aseubel
 * @date 2025-03-11 23:07
 */
@Component
public class PublishDiscussPostEventListener implements ApplicationListener<PublishDiscussPostEvent> {

    @Resource
    private Client meilisearchClient;

    @Override
    public void onApplicationEvent(PublishDiscussPostEvent event) {
        meilisearchClient.index(Constant.getDiscussPostSearchIndex(event.getSchoolCode()))
                .addDocuments(event.toJsonString(), "postId");
    }
}
