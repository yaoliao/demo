package com.example.demo.agent.provider;

import com.example.demo.agent.consumer.ConsumerServer;
import com.example.demo.exchange.Decode;
import com.example.demo.exchange.Encode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class ProviderServer {

    private Logger logger = LoggerFactory.getLogger(ProviderServer.class);

    private static final int PORT = 2222;

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup(8);

    public void startServer() {

        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encode", new Encode());
                            pipeline.addLast("decode", new Decode());
                            pipeline.addLast(new ProviderServerHandler());
                        }
                    });
            Channel channel = bootstrap.bind(PORT).sync().channel();
            logger.info("ProviderServer 启动成功... port : " + PORT);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }


}
