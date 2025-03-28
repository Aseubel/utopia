package com.aseubel.domain.util.service;

import com.aseubel.domain.util.model.ProjectAnalystParam;
import com.aseubel.domain.util.model.RunWorkRequest;
import com.aseubel.types.util.OkHttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Aseubel
 * @description 工具服务
 * @date 2025-03-27 09:45
 */
@Service
@Slf4j
public class UtilService implements IUtilService{

    private static final String workUrl = "https://api.dify.ai/v1/workflows/run";
    private static final String getStatusUrl = "https://api.dify.ai/v1/workflows/run/";

    @Value("${dify.config.project-analyst-token}")
    private String projectToken;

//    @Value("${dify.config.file-convert-token}")
//    private String convertToken;

    @Qualifier("threadPoolExecutor")
    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Override
    public String projectAnalyst(String userId, ProjectAnalystParam param) throws TimeoutException, ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        RunWorkRequest request = new RunWorkRequest(param.toJson());
        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + projectToken,
                "Content-Type", "application/json"
        );
        if (!StringUtils.isEmpty(userId)) {
            request.setUser(userId);
        }
        System.out.println("request: " + request.toJson());
        OkHttpUtil.postSSE(workUrl, headers, OkHttpUtil.createJsonBody(request.toJson()), new OkHttpUtil.SSEEventListener() {
                    @Override
                    public void onEvent(String eventData) {
                        try {
                            JsonObject json = JsonParser.parseString(eventData).getAsJsonObject();
                            if (!future.isDone() && json.has("workflow_run_id")) {
                                String workflowId = json.get("workflow_run_id").getAsString();
                                future.complete(workflowId);
                            }
                        } catch (Exception e) {
                            future.completeExceptionally(new RuntimeException("Invalid event data format"));
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (!future.isDone()) {
                            future.completeExceptionally(new RuntimeException("Server closed connection without sending workflow ID"));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        future.completeExceptionally(t);
                    }
                });

        return future.get(5, TimeUnit.SECONDS);
    }

    @Override
    public String[] getResult(String workFlowId) throws IOException {
        if (StringUtils.isEmpty(workFlowId)) {
            throw new IllegalArgumentException("workFlowId cannot be null or empty");
        }
        String result = OkHttpUtil.doGet(getStatusUrl + workFlowId, Map.of(
                "Authorization", "Bearer " + projectToken,
                "Content-Type", "application/json"
        ));
        JsonObject json = JsonParser.parseString(result).getAsJsonObject();
        String status = json.get("status").getAsString();
        return switch (status) {
            case "running" -> new String[] {"running", "任务正在执行..."};
            case "succeeded" -> new String[] {"succeeded", json.get("outputs").getAsString()};
            case "failed" -> new String[] {"failed", "任务执行失败，请确保提供的参数指向zip文件！" + json.get("error").getAsString()};
            case "stopped" -> new String[] {"stopped", "任务已停止！"};
            default -> new String[] {"unknown", "未知状态！"};
        };
    }
}
