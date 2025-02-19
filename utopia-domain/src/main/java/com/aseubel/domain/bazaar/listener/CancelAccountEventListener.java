package com.aseubel.domain.bazaar.listener;

import com.aseubel.domain.bazaar.adapter.repo.ITradePostRepository;
import com.aseubel.types.event.CancelAccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Aseubel
 * @description 用户注销账号事件监听器
 * @date 2025-02-19 14:09
 */
@Component
@Slf4j
public class CancelAccountEventListener implements ApplicationListener<CancelAccountEvent> {

    @Resource
    private ITradePostRepository tradePostRepository;

    @Override
    public void onApplicationEvent(CancelAccountEvent event) {
        log.info("bazaar domain：监听到用户{}注销账号", event.getUserId());
        tradePostRepository.deleteUncompletedTradePosts(event.getUserId());
    }
}
