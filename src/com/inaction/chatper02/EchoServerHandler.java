package com.inaction.chatper02;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;

/**
 * Created by laiwenqiang on 2018/12/17.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    static final ByteProcessor FIND_SLASH = new ByteProcessor() {
        @Override
        public boolean process(byte b) throws Exception {
            return b != '/';
        }
    };

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.printf("Received: \n" + in.toString(CharsetUtil.UTF_8));
        int endIndex = in.forEachByte(ByteProcessor.FIND_CRLF);
        int startIndex = in.forEachByte(FIND_SLASH) + 1;

        if (endIndex != -1) {
            ByteBuf url = in.readerIndex(startIndex).readBytes(endIndex);
            endIndex = url.forEachByte(ByteProcessor.FIND_ASCII_SPACE);
            url = url.readBytes(endIndex);
            ctx.fireChannelRead(url);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
