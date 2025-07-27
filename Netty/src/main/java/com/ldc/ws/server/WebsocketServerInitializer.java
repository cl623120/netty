package com.ldc.ws.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;

public class WebsocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelGroup group;

    private static final String WEBSOCKET_PATH = "/websocket";

    private final SslContext sslCtx;

    public WebsocketServerInitializer(SslContext sslCtx,ChannelGroup group) {
        this.sslCtx = sslCtx;
        this.group = group;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(socketChannel.alloc()));
        }

        //添加对http的支持
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));

        //Netty提供 支持websocket应答数据压缩传输
        pipeline.addLast(new WebSocketServerCompressionHandler());

        //对整个websocket进行初始化 如果发现Http报文中有升级为websocket的请求包括握手以及以后的一些通信的控制
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));


        //对websocket的数据进行处理
        pipeline.addLast(new WsHandleData(group));
    }
}
