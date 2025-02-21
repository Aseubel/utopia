package com.aseubel.infrastructure.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;

import java.util.List;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public static final AttributeKey<String> URI_KEY = AttributeKey.valueOf("uri");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            ctx.channel().attr(URI_KEY).set(decoder.parameters().get("code").get(0));
            // 可以在这里将 FullHttpRequest 转发到下一个 handler
            ctx.fireChannelRead(request);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        ctx.channel().attr(URI_KEY).set(uri);
        // 可以在这里将 FullHttpRequest 转发到下一个 handler
        ctx.fireChannelRead(request);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
