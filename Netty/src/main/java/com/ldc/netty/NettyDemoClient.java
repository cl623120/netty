package com.ldc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyDemoClient {
    private final int port;
    private final String host;

    public NettyDemoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyDemoClient(9001,"127.0.0.1").start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // 客户端必须使用NioSocketChannel
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new NettyDemoClientHandle());
            //连接到远程节点，阻塞直到连接完成
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
