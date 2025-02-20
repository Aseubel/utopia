package com.aseubel.infrastructure.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

public class HttpHandler extends ChannelInboundHandlerAdapter {
    public static final AttributeKey<String> URI_KEY = AttributeKey.valueOf("uri");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            ctx.channel().attr(URI_KEY).set(uri);
            // 可以在这里将 FullHttpRequest 转发到下一个 handler
            ctx.fireChannelRead(request);
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
