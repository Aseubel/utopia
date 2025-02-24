package com.aseubel.infrastructure.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HeartbeatHandler extends SimpleChannelInboundHandler<PongWebSocketFrame> {
    private static final int HEARTBEAT_INTERVAL = 15; // 心跳间隔(秒)
    private static final int MAX_MISSED_HEARTBEATS = 2; // 允许丢失的心跳次数
    private final Map<ChannelId, Integer> missedHeartbeats = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 添加 IdleStateHandler 触发读空闲事件
        ctx.pipeline().addLast(new IdleStateHandler(HEARTBEAT_INTERVAL * MAX_MISSED_HEARTBEATS, 0, 0));
        scheduleHeartbeat(ctx);
    }

    private void scheduleHeartbeat(ChannelHandlerContext ctx) {
        ctx.executor().scheduleAtFixedRate(() -> {
            if (ctx.channel().isActive()) {
                ctx.writeAndFlush(new PingWebSocketFrame(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8)));
                // 记录丢失的心跳次数
                missedHeartbeats.compute(ctx.channel().id(), (k, v) -> v == null ? 1 : v + 1);
            }
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof PongWebSocketFrame) {
            // 收到 Pong 后重置丢失计数
            missedHeartbeats.remove(ctx.channel().id());
            ctx.fireChannelRead(msg); // 传递消息给后续处理器
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PongWebSocketFrame pongWebSocketFrame) throws Exception {
        System.out.println("收到 Pong 响应");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            int missed = missedHeartbeats.getOrDefault(ctx.channel().id(), 0);
            if (missed >= MAX_MISSED_HEARTBEATS) {
                // 超过最大丢失次数，关闭连接
                System.out.println("连接超时，关闭连接" + ctx.channel().id().asLongText());
                ctx.close();
                cleanOfflineResources(ctx.channel());
            }
        }
    }

    private void cleanOfflineResources(Channel channel) {
        MessageHandler.removeUserChannel(channel);
        missedHeartbeats.remove(channel.id());
    }
}

    // 在HeartbeatHandler中添加异常捕获
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        ctx.close();
//        cleanOfflineResources(ctx.channel());
//    }

