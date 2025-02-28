package com.aseubel.domain.user.listener;

import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.types.event.DeletePostEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Aseubel
 * @description 监听器，用于处理帖子删除事件
 * @date 2025-02-28 12:46
 */
@Component
@Slf4j
public class PostDeleteListener implements ApplicationListener<DeletePostEvent> {

    @Resource
    private IUserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onApplicationEvent(DeletePostEvent event) {
        log.info("user domain: 监听到删除帖子事件");
        CommunityBO communityBO = (CommunityBO) event.getSource();
        userRepository.deleteUserToPost(communityBO.getUserId(), communityBO.getPostId());
    }

}
