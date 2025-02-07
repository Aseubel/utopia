package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.types.event.CustomEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 监听事件
 * @date 2025-02-07 14:55
 */
@Component
public class CommunityListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        DiscussPostEntity post = (DiscussPostEntity) event.getSource();
//        System.out.println("CommunityListener: " + post.getDiscussPostId());
    }
}
