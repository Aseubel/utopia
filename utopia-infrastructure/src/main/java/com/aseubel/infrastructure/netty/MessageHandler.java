package com.aseubel.infrastructure.netty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String message = frame.text();
        String currentUserId = (String) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        // 解析消息格式：{"to":"user456","content":"你好"}
        JsonNode json = new ObjectMapper().readTree(message);
        String toUserId = json.get("to").asText();
        String content = json.get("content").asText();

        Channel targetChannel = AuthHandler.getChannelByUserId(toUserId);
        if (targetChannel != null && targetChannel.isActive()) {
            String response = String.format("{\"from\":\"%s\",\"content\":\"%s\"}",
                    currentUserId, content);
            targetChannel.writeAndFlush(new TextWebSocketFrame(response));
        } else {
            ctx.writeAndFlush(new TextWebSocketFrame("{\"error\":\"对方不在线\"}"));
        }
    }
}

