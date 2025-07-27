package com.ldc.ws.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.ImmediateEventExecutor;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class WebsocketServer {

    //创建 来保存所有已经连接的Websocket channel
    private  final  static ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    static final boolean SSL = false;

    static final int PORT = Integer.parseInt(System.getProperty("com.ldc.ws.server.port", SSL? "8001" : "8080"));

    public static void main(String[] args) throws CertificateException, SSLException {
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();
        } else {
            sslCtx = null;
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebsocketServerInitializer(sslCtx,channelGroup));

            Channel ch = bootstrap.bind(PORT).sync().channel();

            System.out.println("Server started on port " + (SSL ? "https" : "http") + "://127.0.0.1" + PORT + '/');
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
