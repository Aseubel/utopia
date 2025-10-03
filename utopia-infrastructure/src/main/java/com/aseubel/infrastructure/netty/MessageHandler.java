package com.aseubel.infrastructure.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.domain.message.adapter.repo.IMessageRepository;
import com.aseubel.domain.message.model.MessageEntity;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.exception.WxException;
import com.aseubel.types.util.HttpClientUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.aseubel.infrastructure.netty.SessionHandler.userChannels;
import static com.aseubel.types.common.Constant.*;
import static com.aseubel.types.common.RedisKey.REDIS_OFFLINE_KEY;

/**
 * @author Aseubel
 * @description 处理 WebSocket 消息
 * @date 2025-02-21 15:33
 */
@Component
@Slf4j
@Sharable
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolExecutor;

    @Resource
    private IMessageRepository messageRepository;
    @Resource
    private IRedisService redisService;

    // 提供受控的访问方法
    public static void removeUserChannel(Channel channel) {
        userChannels.values().remove(channel);
    }

    public static boolean containsUser(String userId) {
        return userChannels.containsKey(userId);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 当 WebSocket 握手成功后，Netty 会触发此事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof HandshakeComplete) {
            log.info("WebSocket 握手成功. Channel ID: {}", ctx.channel().id().asLongText());
            String userId = ctx.channel().attr(WS_USER_ID_KEY).get();
            if (userId != null) {
                log.info("客户端连接成功，用户id：{}", userId);
                // 握手成功后，发送离线消息
                handleOfflineMessages(ctx, userId);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String userId = getUserIdFromRequest(ctx);
        MessageEntity message = validateMessage(userId, msg);
        saveMessage(message);
        sendOrStoreMessage(message.getToUserId(), message);
    }

    // 处理连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String userId = ctx.channel().attr(WS_USER_ID_KEY).get();
        if (userId != null) {
            Channel removed = userChannels.remove(userId);
            if (removed != null) {
                log.info("客户端断开连接，用户id：{}，Channel ID: {}", userId, ctx.channel().id().asLongText());
            }
        }
        super.channelInactive(ctx);
    }

    /**
     * 最佳实践：在握手成功后处理离线消息
     */
    private void handleOfflineMessages(ChannelHandlerContext ctx, String userId) {
        threadPoolExecutor.execute(() -> {
            try {
                String redisKey = REDIS_OFFLINE_KEY + userId;
                List<String> offlineMsgs = redisService.getListValuesAndRemove(redisKey);
                if (offlineMsgs != null && !offlineMsgs.isEmpty()) {
                    log.info("正在为用户 {} 发送 {} 条离线消息", userId, offlineMsgs.size());
                    offlineMsgs.forEach(msg -> {
                        // 注意：writeAndFlush 会自动处理 ByteBuf 的释放，不需要手动 retain/release
                        ctx.writeAndFlush(new TextWebSocketFrame(msg));
                    });
                }
            } catch (Exception e) {
                log.error("为用户 {} 发送离线消息失败", userId, e);
            }
        });
    }

    private MessageEntity validateMessage(String userId, TextWebSocketFrame textFrame) {
        String message = textFrame.text();
        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String toUserId = json.get("toUserId").getAsString();
            String content = json.get("content").getAsString();
            String type = json.get("type").getAsString();

            if (type.equals("text") || type.equals("image")) {
                content = SensitiveWordHelper.replace(content, '*');
                return new MessageEntity(userId, toUserId, content, type);
            } else {
                throw new AppException("非法的消息类型！");
            }

        } catch (Exception e) {
            throw new AppException("非法的消息格式！");
        }
    }

    private void sendOrStoreMessage(String toUserId, MessageEntity message) {
        String text = buildMessageJson(message);
        Channel targetChannel = userChannels.get(toUserId);
        // 判断用户是否在线
        if (targetChannel != null && targetChannel.isActive()) {
            // 不需要手动 retain/release
            targetChannel.writeAndFlush(new TextWebSocketFrame(text));
        } else {
            storeOfflineMessage(toUserId, text);
        }
    }

    private void storeOfflineMessage(String userId, String content) {
        String redisKey = REDIS_OFFLINE_KEY + userId;
        redisService.addToList(redisKey, content);
        redisService.setListExpired(redisKey, Duration.ofDays(1));
    }

    private void saveMessage(MessageEntity message) {
        threadPoolExecutor.execute(() -> {
            messageRepository.saveMessage(message);
        });
    }

    private String getUserIdFromRequest(ChannelHandlerContext ctx) {
        String userId = ctx.channel().attr(WS_USER_ID_KEY).get();
        // 检查 userId 参数是否存在且非空
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("WebSocket token  is missing or empty");
        }
        return userId;
    }

    private String buildMessageJson(MessageEntity message) {
        return new JSONObject()
                .fluentPut("userId", message.getUserId())
                .fluentPut("toUserId", message.getToUserId())
                .fluentPut("content", message.getContent())
                .fluentPut("type", message.getType())
                .toJSONString();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof AppException appCause) {
            log.error("AppException caught: {}", appCause.getInfo());
        } else if (cause instanceof WxException wxCause) {
            log.error("WxException caught: {}", wxCause.getMessage());
        } else {
            log.error("Exception caught: {}", cause.getMessage(), cause);
        }
        ctx.close(); // 建议关闭发生异常的连接
    }

}
