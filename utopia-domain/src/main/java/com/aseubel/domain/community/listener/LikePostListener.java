package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.types.event.LikeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.aseubel.types.common.Constant.LIKE_POST;

/**
 * @author Aseubel
 * @date 2025/4/19 下午2:29
 */
@Component
@Slf4j
public class LikePostListener implements ApplicationListener<LikeEvent> {

    @Autowired
    private IDiscussPostRepository discussPostRepository;

    @Override
    public void onApplicationEvent(LikeEvent event) {
        discussPostRepository.userBehavior(event.getUserId(), event.getPostId(), LIKE_POST);
    }
}
