package com.millionconnect;

import com.millionconnect.handler.ConnectionCountHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.millionconnect.Constant.BEGIN_PORT;
import static com.millionconnect.Constant.N_PORT;

/**
 * Created by laiwenqiang on 2019/6/1.
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {
        new Server().start(BEGIN_PORT, N_PORT);

    }

    private void start(int beginPort, int nPort) throws InterruptedException {
        System.out.println("Server start....");

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ConnectionCountHandler());
                    }
                });

        for (int i = 0; i < nPort; i++) {
            int port = beginPort + i;
            bootstrap.bind(port).sync().addListener(future -> {
                System.out.println("Bind success in port: " + port);
            });
        }

        System.out.println("Server started!");
    }
}
