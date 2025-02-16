package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.CommentPostEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Aseubel
 * @description 评论帖子事件监听器
 * @date 2025-02-16 15:36
 */
@Component
@Slf4j
public class CommentPostEventListener implements ApplicationListener<CommentPostEvent> {

    @Resource
    private IDiscussPostRepository discussPostRepository;

    @Override
    public void onApplicationEvent(CommentPostEvent event) {
        log.info("监听到评论帖子事件");
        CommunityBO communityBO = (CommunityBO) event.getSource();
        discussPostRepository.increaseCommentCount(communityBO.getPostId());
    }
}
