package com.aseubel.domain.community.task;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 定时任务，统计帖子的点赞、收藏、评论数量
 * @date 2025-02-16 16:17
 */
@Slf4j
@Component
public class CountTask {

    @Autowired
    private IDiscussPostRepository discussPostRepository;

    /**
     * 每分钟执行统计任务
     */
//    @Scheduled(cron = "0 * * * * *")
    public void countTask() {
        log.info("开始统计帖子的点赞、收藏、评论数量");
        discussPostRepository.countAll();
        log.info("统计帖子的点赞、收藏、评论数量完成");
    }

}
