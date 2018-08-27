package com.example.demo.agent.provider;

import com.example.demo.protocol.DubboRpcDecoder;
import com.example.demo.protocol.DubboRpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class ProviderClient {

    private Logger logger = LoggerFactory.getLogger(ProviderClient.class);

    private static final String HOST = "192.168.110.222";
    private static final int PORT = 20880;

    private static Map<String, ProviderClient> thread_bond_provider_client = new ConcurrentHashMap<>(8);

    public static void put(String eventLoopName, ProviderClient client) {
        thread_bond_provider_client.put(eventLoopName, client);
    }

    public static ProviderClient get(String eventLoopName) {
        return thread_bond_provider_client.get(eventLoopName);
    }

    private Bootstrap bootstrap;

    private EventLoop eventLoop;

    private Channel channel;

    public ProviderClient(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    public void init() {

        bootstrap = new Bootstrap();
        bootstrap.group(eventLoop)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("encode", new DubboRpcEncoder())
                                .addLast("decode", new DubboRpcDecoder())
                                .addLast(new ProviderClientHandler());
                    }
                });
//        ChannelFuture connect = bootstrap.connect(HOST, PORT);
        SocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        ChannelFuture connect = bootstrap.connect(socketAddress);
        logger.info("ProviderClient 启动成功... port : " + PORT);
        this.channel = connect.channel();
        thread_bond_provider_client.put(channel.eventLoop().toString(), this);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
