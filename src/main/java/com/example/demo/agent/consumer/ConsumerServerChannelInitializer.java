package com.example.demo.agent.consumer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 *
 * @author Administrator
 * @date 2018/8/16 0016
 */
public class ConsumerServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("encode",new HttpResponseEncoder());
        pipeline.addLast("decode",new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
        pipeline.addLast(new ConsumerServerHandler());
    }
}
