package com.aseubel.domain.search.listener.normal;

import com.aseubel.domain.search.adapter.repo.ISearchRepository;
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

    @Resource
    private ISearchRepository searchRepository;

    @Override
    public void onApplicationEvent(PublishDiscussPostEvent event) {
        event.setImage(searchRepository.getImageUrlByImageId(event.getImage()));
        meilisearchClient.index(Constant.getDiscussPostSearchIndex(event.getSchoolCode()))
                .addDocuments(event.toJsonString(), "postId");
    }
}
