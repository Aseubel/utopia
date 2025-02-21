package com.aseubel.infrastructure.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class TestWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 获取WebSocket握手时的请求参数
        FullHttpRequest request = (FullHttpRequest) ctx.channel().attr(AttributeKey.valueOf("HTTP_REQUEST")).get();
        if (request != null) {
            QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
            String code = queryDecoder.parameters().get("code").get(0);
            System.out.println("Connection code: " + code);
        }
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String receivedText = frame.text();
        System.out.println("Received: " + receivedText);

        // 返回响应
        ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + receivedText));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected: " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
