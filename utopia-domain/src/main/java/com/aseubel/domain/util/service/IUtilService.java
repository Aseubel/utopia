package com.aseubel.domain.util.service;

import com.aseubel.domain.util.model.ProjectAnalystParam;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface IUtilService {

    /**
     * github project analyst
     * @param userId 用户id
     * @param param input
     * @return
     */
    String projectAnalyst(String userId, ProjectAnalystParam param) throws ExecutionException, InterruptedException, TimeoutException;

    /**
     * get result of workflow
     * @param workFlowId
     * @return String数组，第一个元素是workflow的状态，第二个元素是workflow的结果
     */
    String[] getResult(String workFlowId) throws IOException;
}
