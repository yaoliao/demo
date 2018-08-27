package com.example.demo.agent.consumer;

import com.alibaba.fastjson.JSON;
import com.example.demo.exchange.RpcRequest;
import com.example.demo.exchange.RpcResponse;
import com.example.demo.utils.Parsers;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2018/8/16 0016.
 */
public class ConsumerServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Logger logger = LoggerFactory.getLogger(ConsumerServer.class);

    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    private static AtomicLong requestIdGenerator = new AtomicLong(0);

    public static Map<Long, Promise> PROMISE_HOLDER = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        Map<String, String> map = Parsers.fastParser(msg);
        this.callBack(ctx, map);
    }

    private void callBack(ChannelHandlerContext ctx, Map<String, String> map) {

        RpcRequest request = new RpcRequest.Builder().requestID(requestIdGenerator.getAndIncrement())
                .interfaceName(map.get("interface"))
                .methodName(map.get("method"))
                .parameterTypesStr(map.get("parameterTypes")).argumentStr(map.get("parameters")).build();


        Promise<RpcResponse> responsePromise = new DefaultPromise<>(ctx.executor());
        responsePromise.addListener(future -> {
//            @SuppressWarnings("unchecked")
            RpcResponse rpcResponse = (RpcResponse) future.get();
            String resultStr = JSON.toJSONString(rpcResponse.getResult());
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(resultStr.getBytes()));
//            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_TYPE, "application/json;charset=utf-8");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.writeAndFlush(response);
        });

        PROMISE_HOLDER.put(request.getRequestID(), responsePromise);
        Channel channel = ConsumerClient.get(ctx.channel().eventLoop().toString()).channel;
        channel.writeAndFlush(request);

        //responsePromise.trySuccess(map);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //logger.error("http服务器响应出错", cause);
        ctx.channel().close();
    }

}
