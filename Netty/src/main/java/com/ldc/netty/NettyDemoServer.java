package com.ldc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyDemoServer {


    private final int port;

    public NettyDemoServer(int port) {this.port = port;}

    public static void main(String[] args) throws InterruptedException {
        int port = 9001;
        NettyDemoServer nettyDemoServer = new NettyDemoServer(port);
        log.info("nettyDemoClient is running...");
        nettyDemoServer.start();
        log.info("nettyDemoServer is close...");
    }


    public void start() throws InterruptedException {
        final NettyDemoServerHandle nettyDemoServerHandle = new NettyDemoServerHandle();

        //线程组 控制流 多线程处理，并发
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)//将线程组传入
                    .channel(NioServerSocketChannel.class)//使用NIO进行网络传输
                    .localAddress(new InetSocketAddress(port))//服务器监听端口
                    //服务器每接收一个连接请求，就会开启一个socket通信，也就是channel
                    //nettyDemoServerHandle处理具体的业务逻辑
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(nettyDemoServerHandle);
                        }
                    });
            //ChannelFuture异步通知 异步绑定到服务器 阻塞完成
            ChannelFuture f = serverBootstrap.bind().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
