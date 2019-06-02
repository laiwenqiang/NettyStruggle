package com.danmu.server;

import com.danmu.handler.HttpRequestHandler;
import com.danmu.handler.TextWebSocketFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * Created by laiwenqiang on 2019/5/19.
 */
public class DanmuServer {
    private int port;

    public DanmuServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new DanmuServer(8080).start();
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
                                    .addLast("http-decoder", new HttpRequestDecoder())
                                    .addLast("http-response-encode", new HttpObjectAggregator(65535))
                                    .addLast("http-encode", new HttpResponseEncoder())
                                    .addLast("http-chunk", new ChunkedWriteHandler())
                                    .addLast("http-request", new HttpRequestHandler(("/ws")))
                                    .addLast("webSocket-protocol", new WebSocketServerProtocolHandler("/ws"))
                                    .addLast("webSocket-request", new TextWebSocketFrameHandler());
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
