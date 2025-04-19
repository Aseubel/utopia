package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.CommentPostEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.aseubel.types.common.Constant.COMMENT_POST;

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
        CommunityBO communityBO = (CommunityBO) event.getSource();
        if (StringUtils.isNotEmpty(communityBO.getPostId())) {
            discussPostRepository.increaseCommentCount(communityBO.getPostId());
            if (StringUtils.isNotEmpty(communityBO.getUserId())) {
                discussPostRepository.userBehavior(communityBO.getUserId(), communityBO.getPostId(), COMMENT_POST);
            }
        }
    }
}
