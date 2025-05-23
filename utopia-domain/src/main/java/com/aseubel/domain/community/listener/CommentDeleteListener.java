package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.DeleteCommentEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 删除评论事件监听器
 * @date 2025-02-28 16:35
 */
@Component
@Slf4j
public class CommentDeleteListener implements ApplicationListener<DeleteCommentEvent> {

    @Resource
    private ICommentRepository commentRepository;

    @Resource
    private IDiscussPostRepository discussPostRepository;

    @Override
    public void onApplicationEvent(DeleteCommentEvent event) {
        CommunityBO communityBO = (CommunityBO) event.getSource();
        commentRepository.deleteSubComment(communityBO.getCommentId());
        discussPostRepository.decreaseCommentCount(communityBO.getPostId());
    }

}
