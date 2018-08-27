package com.example.demo.agent.provider.demo;

import com.example.demo.agent.provider.ProviderClientHandler;
import com.example.demo.exchange.Decode;
import com.example.demo.exchange.Encode;
import com.example.demo.exchange.RpcRequest;
import com.example.demo.protocol.DubboRpcDecoder;
import com.example.demo.protocol.DubboRpcEncoder;
import com.example.demo.protocol.DubboRpcRequest;
import com.example.demo.protocol.RpcInvocation;
import com.example.demo.utils.JsonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/8/21 0021.
 */
public class DemClient {


    public static void startClint() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new NioEventLoopGroup(8)).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encode", new DubboRpcEncoder())
                                    .addLast("decode", new DubboRpcDecoder())
                                    .addLast(new ProviderClientHandler());
                        }
                    });

            SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 20880);
            ChannelFuture connect = null;
            connect = bootstrap.connect(socketAddress).sync();
            Channel channel = connect.channel();

            CountDownLatch downLatch = new CountDownLatch(1);
            channel.writeAndFlush(convertMsg()).addListener(future -> downLatch.countDown());
            downLatch.await();
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static DubboRpcRequest convertMsg() {
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName("sayHello");
        invocation.setAttachment("path", "com.alibaba.dubbo.demo.DemoService");
        invocation.setParameterTypes("Ljava/lang/String;");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        try {
            JsonUtils.writeObject("Jack", writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        invocation.setArguments(out.toByteArray());

        DubboRpcRequest dubboRpcRequest = new DubboRpcRequest();
        dubboRpcRequest.setId(0L);
        dubboRpcRequest.setVersion("2.0.0");
        dubboRpcRequest.setData(invocation);
        return dubboRpcRequest;
    }

    public static void main(String[] args) {
        startClint();
    }

}
