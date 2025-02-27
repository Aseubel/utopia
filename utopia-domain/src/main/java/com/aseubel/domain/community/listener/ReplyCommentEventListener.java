package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.ReplyCommentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Aseubel
 * @description 回复评论事件监听器
 * @date 2025-02-16 16:08
 */
@Component
@Slf4j
public class ReplyCommentEventListener implements ApplicationListener<ReplyCommentEvent> {

    @Resource
    private IDiscussPostRepository discussPostRepository;

    @Resource
    private ICommentRepository commentRepository;

    @Override
    public void onApplicationEvent(ReplyCommentEvent event) {
        log.info("监听到回复评论事件");
        CommunityBO communityBO = (CommunityBO) event.getSource();
        discussPostRepository.increaseCommentCount(communityBO.getPostId());
        commentRepository.increaseCommentCount(communityBO.getCommentId());
    }
}
