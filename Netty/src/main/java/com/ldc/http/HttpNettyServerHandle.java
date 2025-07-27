package com.ldc.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class HttpNettyServerHandle extends SimpleChannelInboundHandler<HttpObject> {

    private static final byte[] CONTENT = "NettyImpleHttp is Great".getBytes();

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest){
            HttpRequest request = (HttpRequest) httpObject;
            FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK, Unpooled.wrappedBuffer(CONTENT));

            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH,response.content().readableBytes());

            ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(response);
        }
    }
}
