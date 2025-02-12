package com.aseubel.domain.community.listener;

import com.aseubel.types.event.CustomEvent;
import com.aseubel.types.event.LikeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 点赞事件监听处理
 * @date 2025-02-13 00:13
 */
@Component
@Slf4j
public class LikeEventListener implements ApplicationListener<LikeEvent> {
    @Override
    public void onApplicationEvent(LikeEvent event) {
        log.info("收到点赞事件：{}", event.toString());

    }
}
