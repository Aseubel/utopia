package com.aseubel.infrastructure.netty.proto;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.domain.message.adapter.repo.IMessageRepository;
import com.aseubel.domain.message.adapter.serialize.ProtostuffSerializer;
import com.aseubel.domain.message.model.MessageEntity;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.exception.WxException;
import com.aseubel.types.util.HttpClientUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.aseubel.types.common.Constant.*;
import static com.aseubel.types.common.RedisKey.REDIS_OFFLINE_KEY;
import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @author Aseubel
 * @date 2025/4/28 下午11:14
 */
@Component
@Slf4j
@Sharable
public class ProtocolMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Map<String, Channel> userChannels = new ConcurrentHashMap<>();

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object req) throws Exception {
        if (req instanceof FullHttpRequest) {
            String code = getCodeFromRequest(ctx); // 从请求中提取 code
            String userId = code;// getOpenid(code);    // 验证 code 获取 openid

            userChannels.put(userId, ctx.channel());
            ctx.channel().attr(WS_USER_ID_KEY).set(userId);
//            System.out.println("客户端连接成功，用户id：" + userId);
            // 由于这里还在处理握手请求也就是建立连接，所以需要延迟发送离线消息
            new Thread(() -> {
                try {
                    Thread.sleep(50);
                    List<byte[]> offlineMsgs = redisService.getListValuesAndRemove(REDIS_OFFLINE_KEY + userId);
                    if (CollectionUtil.isNotEmpty(offlineMsgs)) {
                        offlineMsgs.forEach(msg -> {
                            ctx.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(msg)));
                        });
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } else if (req instanceof BinaryWebSocketFrame ) {
            this.channelRead0(ctx, (BinaryWebSocketFrame) req);
        } else {
            ctx.fireChannelRead(req);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof BinaryWebSocketFrame) {
            ByteBuf content = ((BinaryWebSocketFrame) frame).content();
            byte[] bytes = new byte[content.readableBytes()];
            content.readBytes(bytes);

            MessageEntity message = ProtostuffSerializer.deserialize(bytes, MessageEntity.class);
            validateMessage(message);
            saveMessage(message);
            sendOrStoreMessage(message.getToUserId(), message);
        } else {
            ctx.close();
        }
    }

    // 处理连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
//        System.out.println("客户端断开连接，用户id：" + ctx.channel().attr(WS_USER_ID_KEY).get());
        Channel channel = ctx.channel();
        for (Map.Entry<String, Channel> entry : userChannels.entrySet()) {
            if (entry.getValue() == channel) {
                userChannels.remove(entry.getKey());
                break;
            }
        }
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
        } finally {
            textFrame.release();
        }
    }

    private void validateMessage(MessageEntity message) {
        if (!"text".equals(message.getType()) && !"image".equals(message.getType())) {
            throw new AppException("非法的消息类型！");
        }
        message.setContent(SensitiveWordHelper.replace(message.getContent(), '*'));
    }

    private void sendOrStoreMessage(String toUserId, MessageEntity message) {
        byte[] messageBytes = ProtostuffSerializer.serialize(message);
        if (isUserOnline(toUserId)) {
            sendMessage(toUserId, new BinaryWebSocketFrame(Unpooled.wrappedBuffer(messageBytes)));
        } else {
            storeOfflineMessage(toUserId, messageBytes);
        }
    }

    private void sendMessage(String userId, WebSocketFrame message) {
        Channel targetChannel = userChannels.get(userId);
        if (targetChannel != null && targetChannel.isActive()) {
            targetChannel.writeAndFlush(message.retain());
        }
    }

    private void storeOfflineMessage(String userId, byte[] content) {
        redisService.addToList(REDIS_OFFLINE_KEY + userId, content);
        redisService.setListExpired(REDIS_OFFLINE_KEY + userId, Duration.ofDays(1));
    }

    private void saveMessage(MessageEntity message) {
//        threadPoolExecutor.execute(() -> {
//            messageRepository.saveMessage(message);
//        });
    }

    private boolean isUserOnline(String userId) {
        return userChannels.containsKey(userId);
    }

    private String getCodeFromRequest(ChannelHandlerContext ctx) {
        String code = ctx.channel().attr(WS_TOKEN_KEY).get();
        // 检查 code 参数是否存在且非空
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("WebSocket token  is missing or empty");
        }
        return code;
    }

    private String getOpenid(String appid, String secret, String code) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appid);
        paramMap.put("secret", secret);
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        String result = HttpClientUtil.doGet(WX_LOGIN, paramMap);

        //获取请求结果
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        //判断openid是否存在
        if (StringUtils.isEmpty(openid)) {
            throw new WxException(jsonObject.getString("errcode"), jsonObject.getString("errmsg"));
        }

        return openid;
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
