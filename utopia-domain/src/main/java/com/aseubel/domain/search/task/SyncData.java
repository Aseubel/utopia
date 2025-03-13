package com.aseubel.domain.search.task;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.search.adapter.repo.ISearchDiscussPostRepository;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 同步帖子数据
 * @date 2025-03-12 11:16
 */
@Slf4j
@Component
@EnableAsync
public class SyncData {

    @Autowired
    private ISearchDiscussPostRepository searchRepository;

    @Resource
    private Client meilisearchClient;

    /**
     * 同步帖子数据
     * 每五分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void syncData() {
        log.info("同步帖子数据");

    }

}
