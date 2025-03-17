package com.aseubel.domain.search.listener.normal;

import com.aseubel.types.common.Constant;
import com.aseubel.types.event.DeleteDiscussPostEvent;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;


/**
 * @author Aseubel
 * @date 2025-03-11 23:06
 */
//@Component
public class DeleteDiscussPostEventListener implements ApplicationListener<DeleteDiscussPostEvent> {

    @Resource
    private Client meilisearchClient;

    @Override
    public void onApplicationEvent(DeleteDiscussPostEvent event) {
        meilisearchClient.index(Constant.getDiscussPostSearchIndex(event.getSchoolCode())).deleteDocument(event.getPostId());
    }
}
