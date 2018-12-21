package com.inaction.chatper02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by laiwenqiang on 2018/12/17.
 */
public class RequestController extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String path = in.toString(CharsetUtil.UTF_8);
        System.out.printf("Do business, path = " + path);

        String result = ActionMapping.getMappingRequest(path);
        if (result == null) {
            result = "404";
        }

        ctx.write(Unpooled.copiedBuffer(result.getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
