package com.aseubel.infrastructure.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.exception.WxException;
import com.aseubel.types.util.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
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
public class MessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Map<String, Queue<WebSocketFrame>> OFFLINE_MSGS = new ConcurrentHashMap<>();

    private static final Map<String, Channel> userChannels = new ConcurrentHashMap<>();

    // 提供受控的访问方法
    public static void removeUserChannel(Channel channel) {
        userChannels.values().remove(channel);
    }

    public static boolean containsUser(String userId) {
        return userChannels.containsKey(userId);
    }

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
                    OFFLINE_MSGS.getOrDefault(userId, new LinkedList<>())
                            .forEach(ctx::writeAndFlush);
                    OFFLINE_MSGS.remove(userId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } else if (req instanceof TextWebSocketFrame ) {
            this.channelRead0(ctx, (TextWebSocketFrame) req);
        } else {
            ctx.fireChannelRead(req);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            handleTextFrame(ctx, (TextWebSocketFrame) frame);
        } else {
            ctx.close();
        }
    }

    // 处理连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端断开连接：" + ctx.channel());
        Channel channel = ctx.channel();
        for (Map.Entry<String, Channel> entry : userChannels.entrySet()) {
            if (entry.getValue() == channel) {
                userChannels.remove(entry.getKey());
                break;
            }
        }
    }

    private void handleTextFrame(ChannelHandlerContext ctx, TextWebSocketFrame textFrame) {
        String message = textFrame.text();
        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String toUserId = json.get("toUserId").getAsString();
            String content = json.get("content").getAsString();
            String type = json.get("type").getAsString();

            if (type.equals("text") || type.equals("image")) {
                sendOrStoreMessage(toUserId, new TextWebSocketFrame(message));
            } else {
                throw new AppException("Invalid message type");
            }

        } catch (Exception e) {
            throw new AppException("Invalid text message format");
        }
    }

    private void handleBinaryFrame(ChannelHandlerContext ctx, BinaryWebSocketFrame binaryFrame) {
        ByteBuf contentBuf = binaryFrame.content();
        //读取所有可读的字节
        while (contentBuf.isReadable()) {
            System.out.println(contentBuf.readByte());
        }

        // 解析消息格式：前4字节为用户ID长度，后接用户ID，剩余为图片数据
        if (contentBuf.readableBytes() < 4) {
            throw new AppException("Invalid binary message format");
        }

        int userIdLength = contentBuf.readInt();
        if (contentBuf.readableBytes() < userIdLength) {
            throw new AppException("Invalid binary message format");
        }

        byte[] userIdBytes = new byte[userIdLength];
        contentBuf.readBytes(userIdBytes);
        String toUserId = new String(userIdBytes, StandardCharsets.UTF_8);

        byte[] imageData = new byte[contentBuf.readableBytes()];
        contentBuf.readBytes(imageData);

        // 保存图片数据并转发
        sendOrStoreMessage(toUserId, new BinaryWebSocketFrame(Unpooled.wrappedBuffer(imageData)));
    }

    private void sendOrStoreMessage(String toUserId, WebSocketFrame message) {
        if (isUserOnline(toUserId)) {
            Channel targetChannel = userChannels.get(toUserId);
            if (targetChannel != null && targetChannel.isActive()) {
                targetChannel.writeAndFlush(message.retain());
            }
        } else {
            // 存储原始WebSocketFrame（需保留引用）
            OFFLINE_MSGS.computeIfAbsent(toUserId, k -> new LinkedList<>())
                    .add(message.retain());
        }
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
