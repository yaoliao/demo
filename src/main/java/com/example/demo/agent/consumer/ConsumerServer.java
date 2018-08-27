package com.example.demo.agent.consumer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @date 2018/8/16 0016
 */
public class ConsumerServer {

    private Logger logger = LoggerFactory.getLogger(ConsumerServer.class);

    private static final int PORT = 2333;

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private EventLoopGroup workerGroup = new NioEventLoopGroup(8);


    public void startServer() {

        initConsumerClient(workerGroup);

        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ConsumerServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            Channel channel = bootstrap.bind(PORT).sync().channel();
            logger.info("ConsumerServer 启动成功... port : " + PORT);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private void initConsumerClient(EventLoopGroup eventLoopGroup) {
        for (EventExecutor executor : eventLoopGroup) {
            if (executor instanceof EventLoop) {
                ConsumerClient client = new ConsumerClient((EventLoop) executor);
                client.init();
                ConsumerClient.put(executor.toString(), client);
            }
        }
    }

}
