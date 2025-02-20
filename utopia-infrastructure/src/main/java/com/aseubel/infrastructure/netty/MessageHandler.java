package com.aseubel.infrastructure.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.types.exception.WxException;
import com.aseubel.types.util.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static com.aseubel.types.common.Constant.WX_LOGIN;

// 离线消息处理器
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final AttributeKey<String> WS_TOKEN_KEY = AttributeKey.valueOf("code");

    private static final Map<String, Queue<String>> OFFLINE_MSGS = new ConcurrentHashMap<>();

    private static final Map<String, Channel> userChannels = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String code = getCodeFromRequest(ctx); // 从请求中提取 code
        String userId = code;// validateCode(code);    // 验证 code 获取 openid
        userChannels.put(userId, ctx.channel());
        OFFLINE_MSGS.getOrDefault(userId, new LinkedList<>())
                .forEach(msg -> ctx.writeAndFlush(new TextWebSocketFrame(msg)));
        OFFLINE_MSGS.remove(userId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String message = msg.text();
        // 解析消息（假设消息格式为 {"toUserId":"xxx","content":"hello"}）
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        String toUserId = json.get("toUserId").getAsString();
        String content = json.get("content").getAsString();
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
    }

    // 处理连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
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
