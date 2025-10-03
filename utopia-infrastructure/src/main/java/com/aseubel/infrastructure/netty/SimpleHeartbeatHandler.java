package com.aseubel.infrastructure.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Aseubel
 * @date 2025/10/3 下午7:22
 * @description 利用 IdleStateHandler 检测并关闭空闲（死亡）连接
 */
@Slf4j
public class SimpleHeartbeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("检测到读超时，关闭不活跃的连接. Channel ID: {}", ctx.channel().id().asLongText());
            // 触发读空闲事件，我们认为客户端已经断开
            // 关闭连接会触发 MessageHandler 的 channelInactive 方法，进行资源清理
            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
