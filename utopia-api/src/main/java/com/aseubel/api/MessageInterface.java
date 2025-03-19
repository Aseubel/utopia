package com.aseubel.api;

import com.aseubel.api.dto.message.QueryMessageRequest;
import com.aseubel.api.dto.message.QueryMessageResponse;
import com.aseubel.api.dto.message.ReadMessageRequest;
import com.aseubel.types.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-03-19 22:04
 */
public interface MessageInterface {

    /**
     * 查询消息
     * @param queryMessageRequest
     * @return
     */
    Response<List<QueryMessageResponse>> queryMessage(QueryMessageRequest queryMessageRequest);

    /**
     * 标记消息为已读
     * @param readMessageRequest
     * @return
     */
    Response readMessage(@Valid @RequestBody ReadMessageRequest readMessageRequest);
}
