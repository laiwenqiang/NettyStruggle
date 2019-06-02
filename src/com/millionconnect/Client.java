package com.millionconnect;

import com.millionconnect.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import static com.millionconnect.Constant.BEGIN_PORT;
import static com.millionconnect.Constant.N_PORT;


/**
 * Created by laiwenqiang on 2019/6/1.
 */
public class Client {

//    private static String SERVER_HOST = "192.168.59.1";
    private static String SERVER_HOST = "localhost";

    public static void main(String[] args) throws InterruptedException {
        new Client().start(BEGIN_PORT, N_PORT);

    }

    public void start(int beginPort, int nPort) throws InterruptedException {
        System.out.println("Client start...");
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        for (int i = 0; i < nPort; i++) {
            int port = beginPort + i;
            System.out.println("Port: " + port);
            for (int k = 0; k < 10000; k++) {
                System.out.println("Num: " + k);
                bootstrap.connect(SERVER_HOST, port).sync();
            }
        }


//        int index = 0;
//        int port;

//        while (!Thread.interrupted()) {
//            port = beginPort + index;
//
//            System.out.println("Port: " + port);
//
//            Future future = bootstrap.connect(SERVER_HOST, port);
//            future.addListener(futurel -> {
//                if (!futurel.isSuccess()) {
//                    System.out.println("Connect failed, exit!");
////                    System.exit(0);
//                }
//            });
//
//            if (++index == nPort) {
//                index = 0;
//            }
//        }
    }
}
