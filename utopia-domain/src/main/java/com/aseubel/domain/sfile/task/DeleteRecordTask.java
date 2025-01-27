package com.aseubel.domain.sfile.task;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 定时任务，数据库记录
 * @date 2025-01-20 18:39
 */
@Slf4j
@Component
public class DeleteRecordTask {

    @Autowired
    private IFileRepository fileRepository;

    /**
     * 每天凌晨0点执行一次，删除数据库中重复的sfile记录
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldData() {
        log.info("开始删除重复的sfile数据库记录");
        fileRepository.deleteRepeatedSFile();
    }

    /**
     * 每天凌晨0点执行一次，删除oss对象不存在的sfile记录
     */
    @Scheduled(cron = "1 0 0 * * ?")
    public void deleteMissingData() throws ClientException {
        log.info("开始删除oss对象不存在的sfile数据库记录");
        fileRepository.deleteMissingSFile();
    }

}
