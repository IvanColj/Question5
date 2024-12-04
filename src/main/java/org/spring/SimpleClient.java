package org.spring;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class SimpleClient extends ChannelInboundHandlerAdapter {

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new SimpleClientHandler());
                        }
                    })
                    .connect("localhost", 1234)
                    .sync()
                    .channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = in.readLine();
                if (Objects.equals(input, "exit")) {
                    break;
                }
                channel.writeAndFlush(input);
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
