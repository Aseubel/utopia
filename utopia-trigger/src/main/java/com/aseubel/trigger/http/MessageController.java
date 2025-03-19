package com.aseubel.trigger.http;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.api.MessageInterface;
import com.aseubel.api.dto.message.QueryMessageRequest;
import com.aseubel.api.dto.message.QueryMessageResponse;
import com.aseubel.api.dto.message.ReadMessageRequest;
import com.aseubel.domain.message.model.MessageBO;
import com.aseubel.domain.message.model.MessageEntity;
import com.aseubel.domain.message.service.IMessageService;
import com.aseubel.types.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/message/") //${app.config.api-version}
@RequiredArgsConstructor
public class MessageController implements MessageInterface {

    private final IMessageService messageService;

    /**
     * 查询消息
     */
    @Override
    @GetMapping("")
    public Response<List<QueryMessageResponse>> queryMessage(QueryMessageRequest queryMessageRequest) {
        MessageBO messageBO = MessageBO.builder()
                   .userId(queryMessageRequest.getUserId())
                   .otherId(queryMessageRequest.getOtherId())
                   .messageId(queryMessageRequest.getMessageId())
                   .limit(queryMessageRequest.getLimit())
                   .build();
        List<MessageEntity> messages = messageService.getMessages(messageBO);
        if (CollectionUtil.isEmpty(messages)) {
            return Response.SYSTEM_SUCCESS(List.of());
        }
        List<QueryMessageResponse> queryMessageResponses = messages.stream()
               .map(message -> QueryMessageResponse.builder()
                       .messageId(message.getMessageId())
                       .userId(message.getUserId())
                       .toUserId(message.getToUserId())
                       .content(message.getContent())
                       .type(message.getType())
                       .time(message.getSendTime())
                       .build())
               .toList();
        return Response.SYSTEM_SUCCESS(queryMessageResponses);
    }

    /**
     * 消息已读
     */
    @Override
    @PutMapping("")
    public Response readMessage(@Valid @RequestBody ReadMessageRequest readMessageRequest) {
        MessageBO messageBO = MessageBO.builder()
                .userId(readMessageRequest.getUserId())
                .otherId(readMessageRequest.getOtherId())
                .messageId(readMessageRequest.getMessageId())
                .build();
        messageService.readMessage(messageBO);
        return Response.SYSTEM_SUCCESS();
    }

}
