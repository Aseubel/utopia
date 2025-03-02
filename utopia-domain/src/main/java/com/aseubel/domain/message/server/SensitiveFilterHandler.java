package com.aseubel.domain.message.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Set;

// 敏感词过滤处理器
public class SensitiveFilterHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Set<String> SENSITIVE_WORDS = null; // Set.of("暴力", "色情");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String filtered = SENSITIVE_WORDS.stream()
                .reduce(msg.text(), (text, word) -> text.replaceAll(word, "**"));
        ctx.fireChannelRead(new TextWebSocketFrame(filtered));
    }
}
