package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.FavoriteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Aseubel
 * @description 收藏事件监听器
 * @date 2025-02-14 20:32
 */
@Component
@Slf4j
public class FavoriteEventListener implements ApplicationListener<FavoriteEvent> {

    @Resource
    private IDiscussPostRepository discussPostRepository;

    @Override
    public void onApplicationEvent(FavoriteEvent event) {
        log.info("监听到收藏事件");
        CommunityBO communityBO = (CommunityBO) event.getSource();
        discussPostRepository.favoritePost(communityBO.getUserId(), communityBO.getPostId());
    }
}
