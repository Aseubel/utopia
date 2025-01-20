package com.aseubel.domain.sfile.task;

import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 定时任务，删除重复的sfile数据库记录
 * @date 2025-01-20 18:39
 */
@Slf4j
@Component
public class DeleteRepeatedTask {

    @Autowired
    private IFileRepository fileRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldData() {
        log.info("开始删除重复的sfile数据库记录");
        fileRepository.deleteRepeatedSFile();
    }

}
