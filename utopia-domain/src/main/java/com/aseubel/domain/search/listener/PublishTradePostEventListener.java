package com.aseubel.domain.search.listener;

import com.aseubel.domain.search.adapter.repo.ISearchRepository;
import com.aseubel.types.common.Constant;
import com.aseubel.types.event.PublishTradePostEvent;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * @author Aseubel
 * @date 2025-03-11 23:07
 */
@Component
public class PublishTradePostEventListener implements ApplicationListener<PublishTradePostEvent> {

    @Resource
    private Client meilisearchClient;

    @Resource
    private ISearchRepository searchRepository;

    @Override
    public void onApplicationEvent(PublishTradePostEvent event) {
        event.setImage(searchRepository.getImageUrlByImageId(event.getImage()));
        meilisearchClient.index(Constant.getTradePostSearchIndex(event.getSchoolCode()))
                .addDocuments(event.toJsonString(), "postId");
    }
}
