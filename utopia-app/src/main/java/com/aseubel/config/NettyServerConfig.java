package com.aseubel.config;

import com.aseubel.infrastructure.netty.HeartbeatHandler;
import com.aseubel.infrastructure.netty.HttpHandler;
import com.aseubel.infrastructure.netty.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.concurrent.TimeUnit;

import static com.aseubel.types.common.Constant.NETTY_PORT;

@Component
public class NettyServerConfig {

    private ChannelFuture serverChannelFuture;

    // 心跳间隔(秒)
    private static final int HEARTBEAT_INTERVAL = 15;
    // 读超时时间
    private static final int READ_TIMEOUT = HEARTBEAT_INTERVAL * 2;

    // 使用线程池管理
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    public void startNettyServer() {
        // 使用独立线程启动Netty服务
        new Thread(() -> {
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel ch) {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new HttpServerCodec());
                                pipeline.addLast(new HttpObjectAggregator(10 * 1024 * 1024));// 最大10MB
                                pipeline.addLast(new ChunkedWriteHandler());
                                pipeline.addLast(new HttpHandler());
                                pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                                pipeline.addLast(new IdleStateHandler(0, 0, 0, TimeUnit.SECONDS));
                                pipeline.addLast(new HeartbeatHandler());
                                pipeline.addLast(new MessageHandler());
                            }
                        });

                serverChannelFuture = bootstrap.bind(NETTY_PORT).sync();
                System.out.println("WebSocket server started on port " + NETTY_PORT);

                // 保持通道开放
                serverChannelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @PreDestroy
    public void stopNettyServer() {
        // 优雅关闭
        if (serverChannelFuture != null) {
            serverChannelFuture.channel().close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    // 新增SSL配置
//    @Bean
//    public SslContext sslContext() throws Exception {
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        try (InputStream in = getClass().getResourceAsStream("/keystore.pfx")) {
//            keyStore.load(in, "password".toCharArray());
//        }
//        return SslContextBuilder.forServer(keyStore.getCertificate("alias"),
//                        keyStore.getKey("alias", "password".toCharArray()))
//                .build();
//    }

}

/**
 * // 建立WebSocket连接
 * const socket = wx.connectSocket({
 * url: 'ws://your-domain.com/ws?token=用户登录凭证'
 * })
 * <p>
 * // 监听消息
 * socket.onMessage(res => {
 * console.log('收到消息:', res.data)
 * })
 * <p>
 * // 发送消息
 * function sendMessage(toUserId, content) {
 * const msg = JSON.stringify({
 * to: toUserId,
 * content: content
 * })
 * socket.send(msg)
 * }
 */