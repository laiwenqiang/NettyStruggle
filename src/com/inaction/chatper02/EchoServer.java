package com.inaction.chatper02;

import com.inaction.chatper02.handler.InboundHandlerA;
import com.inaction.chatper02.handler.InboundHandlerB;
import com.inaction.chatper02.handler.InboundHandlerC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by laiwenqiang on 2018/12/17.
 */
public class EchoServer {
    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new EchoServer(8888).start();
    }

    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new InboundHandlerA())
                                    .addLast(new InboundHandlerB())
                                    .addLast(new InboundHandlerC());
//                                    .addLast(new EchoServerHandler())
//                                    .addLast(new RequestController());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }
}
