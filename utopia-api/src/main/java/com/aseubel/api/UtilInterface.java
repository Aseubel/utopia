package com.aseubel.api;

import com.aseubel.api.dto.util.ProjectAnalystRequest;
import com.aseubel.api.dto.util.UtilResultResponse;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author Aseubel
 * @description 工具接口
 * @date 2025-03-27 00:59
 */
public interface UtilInterface {

    Response<String> projectAnalyst(@RequestBody ProjectAnalystRequest request) throws ExecutionException, InterruptedException, TimeoutException;

    Response<UtilResultResponse> getUtilResult(String workFlowId);
}
