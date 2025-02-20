package com.aseubel.config;

import com.aseubel.infrastructure.netty.AuthHandler;
import com.aseubel.infrastructure.netty.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@EnableConfigurationProperties(NettyServerConfigProperties.class)
public class NettyServerConfig {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Bean
    public ChannelInitializer<SocketChannel> channelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
//                pipeline.addLast(new AuthHandler());
//                pipeline.addLast(new MessageHandler());
            }
        };
    }

    @Bean
    public ServerBootstrap serverBootstrap(NettyServerConfigProperties properties) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer());
        b.bind(21611);
        return b;
    }

    @PreDestroy
    public void shutdown() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().syncUninterruptibly();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().syncUninterruptibly();
        }
    }
}

/**
 * // 建立WebSocket连接
 * const socket = wx.connectSocket({
 *   url: 'ws://your-domain.com/ws?token=用户登录凭证'
 * })
 *
 * // 监听消息
 * socket.onMessage(res => {
 *   console.log('收到消息:', res.data)
 * })
 *
 * // 发送消息
 * function sendMessage(toUserId, content) {
 *   const msg = JSON.stringify({
 *     to: toUserId,
 *     content: content
 *   })
 *   socket.send(msg)
 * }
 */