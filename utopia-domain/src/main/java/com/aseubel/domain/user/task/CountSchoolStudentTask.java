package com.aseubel.domain.user.task;

import com.aseubel.domain.user.adapter.repo.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 定时任务，统计学校学生（用户）数量
 * @date 2025-02-05 15:15
 */
@Slf4j
@Component
public class CountSchoolStudentTask {

    @Autowired
    private IUserRepository userRepository;

    /**
     * 每天凌晨 4 点统计学校学生数量
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void countSchoolStudent() {
        log.info("task: 开始统计学校学生数量！");
        userRepository.updateSchoolStudentCount();
        log.info("task: 统计学校学生数量完成！");
    }

}
