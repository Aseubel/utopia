package com.aseubel.domain.community.task;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 定时任务，删除不存在的图片数据库记录
 * @date 2025-02-16 17:15
 */
@Slf4j
@Component
public class DeleteImageTask {

    @Autowired
    private IDiscussPostRepository discussPostRepository;

    @Autowired
    private ICommentRepository commentRepository;

    /**
     * 每天执行一次，删除数据库中重复的sfile记录
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteMissingImage() throws ClientException {
        log.info("task: 删除不存在的image数据库记录");
        discussPostRepository.deleteMissingImage();
    }

}
