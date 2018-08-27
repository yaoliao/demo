package com.example.demo.agent.consumer;

import com.example.demo.exchange.Decode;
import com.example.demo.exchange.Encode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class ConsumerClient {

    private Logger logger = LoggerFactory.getLogger(ConsumerClient.class);

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 2222;

    private static Map<String, ConsumerClient> thread_bond_consumer_client = new HashMap<>(8);

    public static void put(String eventLoopName, ConsumerClient client) {
        thread_bond_consumer_client.put(eventLoopName, client);
    }

    public static ConsumerClient get(String eventLoopName) {
        return thread_bond_consumer_client.get(eventLoopName);
    }

    private volatile boolean available = false;

    public Channel channel = null;

    private EventLoop sharedEventLoop;

    public ConsumerClient(EventLoop sharedEventLoop) {
        this.sharedEventLoop = sharedEventLoop;
    }

    public void init() {
        this.channel = connect();
        available = true;
    }

    public Channel connect() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(sharedEventLoop).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("encode", new Encode());
                        pipeline.addLast("decode", new Decode());
                        pipeline.addLast(new ConsumerClientHandler());
                    }
                });
        ChannelFuture connect = bootstrap.connect(HOST, PORT);
        logger.info("ConsumerClient 启动成功 ..... port : "  + PORT);
        return connect.channel();

    }
}
