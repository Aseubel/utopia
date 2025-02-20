package com.aseubel.infrastructure.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 存储在线用户
    private static final Map<String, Channel> onlineUsers = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String text = frame.text();
        if (text.startsWith("AUTH:")) {
            String token = text.substring(5);
            // 这里需要实现token验证逻辑，获取用户ID
            String userId = verifyToken(token);

            onlineUsers.put(userId, ctx.channel());
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
            ctx.writeAndFlush(new TextWebSocketFrame("AUTH_SUCCESS"));
            ctx.pipeline().remove(this); // 认证通过后移除本处理器
        } else {
            ctx.close();
        }
    }

    private String verifyToken(String token) {
        // 实现实际的token验证逻辑，返回用户ID
        return "user123";
    }

    public static Channel getChannelByUserId(String userId) {
        return onlineUsers.get(userId);
    }
}

