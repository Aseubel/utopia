package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.LikeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Aseubel
 * @description 点赞事件监听处理
 * @date 2025-02-13 00:13
 */
@Component
@Slf4j
public class LikeEventListener implements ApplicationListener<LikeEvent> {

    @Resource
    private IDiscussPostRepository discussPostRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onApplicationEvent(LikeEvent event) {
        log.info("监听到点赞事件");
        CommunityBO communityBO = (CommunityBO) event.getSource();
        if (discussPostRepository.likePost(communityBO.getUserId(), communityBO.getPostId(), communityBO.getEventTime())) {
            discussPostRepository.increaseLikeCount(communityBO.getPostId());
        } else {
            discussPostRepository.decreaseLikeCount(communityBO.getPostId());
        }
    }
}
