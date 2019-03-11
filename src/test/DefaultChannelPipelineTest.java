package test;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * Created by laiwenqiang on 2019/3/11.
 */
public class DefaultChannelPipelineTest {
    public static void main(String[] args) {
        Channel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new Handler(), new Handler());

        System.out.println(channel.pipeline());
        System.out.println("---- before fireChannelRegistered ---");

        channel.pipeline().fireChannelRegistered();

        System.out.println("---- before fireChannelRead ----");
        channel.pipeline().fireChannelRead("Hello");
    }

}

class Handler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ctx.name() + ":" + msg);
        ctx.fireChannelRead("World");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.name() + ": exceptionCaught." + cause);
    }
}
