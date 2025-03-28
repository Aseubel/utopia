package com.aseubel.trigger.http;

import com.aseubel.api.UtilInterface;
import com.aseubel.api.dto.util.ProjectAnalystRequest;
import com.aseubel.api.dto.util.UtilResultResponse;
import com.aseubel.domain.util.model.ProjectAnalystParam;
import com.aseubel.domain.util.service.IUtilService;
import com.aseubel.types.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 工具
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/util/") //${app.config.api-version}
@RequiredArgsConstructor
public class UtilController implements UtilInterface {

    private final IUtilService utilService;

    /**
     * 项目分析
     */
    @Override
    @PostMapping("/analyze")
    public Response<String> projectAnalyst(@RequestBody ProjectAnalystRequest request) throws ExecutionException, InterruptedException, TimeoutException {
        ProjectAnalystParam param = ProjectAnalystParam.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .repo(request.getRepoName())
                .ref(request.getBranch())
                .file_url(request.getFileUrl())
                .build();
        String result = utilService.projectAnalyst(request.getUserId(), param);
        return Response.SYSTEM_SUCCESS(result);
    }

    /**
     * 获取结果
     */
    @Override
    @GetMapping("/result/{workFlowId}")
    public Response<UtilResultResponse> getUtilResult(@PathVariable String workFlowId) {
        try {
            String[] result = utilService.getResult(workFlowId);
            return Response.SYSTEM_SUCCESS(new UtilResultResponse(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
