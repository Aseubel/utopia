package com.aseubel.domain.message.server;

import com.aseubel.types.exception.AppException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import static com.aseubel.types.common.Constant.WS_TOKEN_KEY;

/**
 * @author Aseubel
 * @description 处理websocket连接请求，将code参数存入channel的attribute中
 * @date 2025-02-21 15:34
 */
public class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 判断是否是连接请求
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            try {
                QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
                ctx.channel().attr(WS_TOKEN_KEY).set(decoder.parameters().get("code").get(0));
            } catch (Exception e) {
                throw new AppException("非法的websocket连接请求");
            }
            // 将 FullHttpRequest 转发到下一个 handler
            ctx.fireChannelRead(request);
            // 重新设置 uri，将请求转发到 websocket handler，否则无法成功建立连接
            request.setUri("/ws");
//            ctx.fireChannelRead(request);
        }
        // 消息直接交给下一个 handler
        super.channelRead(ctx, msg);
    }

}
