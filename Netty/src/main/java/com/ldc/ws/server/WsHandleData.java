package com.ldc.ws.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.Locale;
import java.util.logging.Logger;

public class WsHandleData extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final ChannelGroup channelGroup;

    public WsHandleData(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    private static final Logger logger = Logger.getLogger(WsHandleData.class.getName());
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        //判断是否为文本帧 目前只处理文本帧
        if (webSocketFrame instanceof TextWebSocketFrame){
            String text = ((TextWebSocketFrame) webSocketFrame).text();
            channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(text.toUpperCase(Locale.CHINA)));
            channelGroup.writeAndFlush(new TextWebSocketFrame("client" + channelHandlerContext.channel() +"say" + text.toUpperCase(Locale.CHINA)));
        }else {
            String message = "frame type not supported:" + webSocketFrame.getClass().getName();
            logger.warning(message);
        }
    }
}
