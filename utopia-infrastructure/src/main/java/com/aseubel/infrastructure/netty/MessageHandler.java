package com.aseubel.infrastructure.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.exception.WxException;
import com.aseubel.types.util.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static com.aseubel.types.common.Constant.WS_TOKEN_KEY;
import static com.aseubel.types.common.Constant.WX_LOGIN;

/**
 * @author Aseubel
 * @description 处理 WebSocket 消息
 * @date 2025-02-21 15:33
 */
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Map<String, Queue<String>> OFFLINE_MSGS = new ConcurrentHashMap<>();

    private static final Map<String, Channel> userChannels = new ConcurrentHashMap<>();

    private static final Map<String, Channel> recordChannels = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接：" + ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object req) throws Exception {
        if (req instanceof FullHttpRequest) {
            String code = getCodeFromRequest(ctx); // 从请求中提取 code
            String userId = code;// validateCode(code);    // 验证 code 获取 openid
            userChannels.put(userId, ctx.channel());
            // 由于这里还在处理握手请求也就是建立连接，所以需要延迟发送离线消息
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                OFFLINE_MSGS.getOrDefault(userId, new LinkedList<>())
                        .forEach(msg -> ctx.writeAndFlush(new TextWebSocketFrame(msg)));
                OFFLINE_MSGS.remove(userId);
            }).start();
        } else {
            this.channelRead0(ctx, (TextWebSocketFrame) req);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        System.out.println("Received message: " + msg.text());
        String message = msg.text();
        // 解析消息（假设消息格式为 {"toUserId":"xxx","content":"hello"}）
        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String toUserId = json.get("toUserId").getAsString();
            String content = json.get("content").getAsString();
            System.out.println("user：" + ctx.channel() + "toUserId: " + toUserId + ", content: " + content);

            if (isUserOnline(toUserId)) {
                // 查找目标用户的 Channel
                Channel targetChannel = userChannels.get(toUserId);
                if (targetChannel != null && targetChannel.isActive()) {
                    targetChannel.writeAndFlush(new TextWebSocketFrame(content));
                }
            } else {
                OFFLINE_MSGS.computeIfAbsent(toUserId, k -> new LinkedList<>())
                        .add(content);
            }
        } catch (IllegalStateException e) {
            throw new AppException("Invalid message format");
        }
    }

    // 处理连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端断开连接：" + ctx.channel());
        userChannels.values().remove(ctx.channel());
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

}
