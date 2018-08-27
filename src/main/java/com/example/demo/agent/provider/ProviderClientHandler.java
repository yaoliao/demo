package com.example.demo.agent.provider;

import com.example.demo.agent.consumer.ConsumerClient;
import com.example.demo.exchange.RpcResponse;
import com.example.demo.protocol.DubboRpcResponse;
import com.example.demo.serializer.SerializerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class ProviderClientHandler extends SimpleChannelInboundHandler<Object> {

    private Logger logger = LoggerFactory.getLogger(ProviderClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof List) {
            List<DubboRpcResponse> responses = (List<DubboRpcResponse>) msg;
            for (DubboRpcResponse response : responses) {
                process(response);
            }
        } else if (msg instanceof DubboRpcResponse) {
            process((DubboRpcResponse) msg);
        }
    }

    private void process(DubboRpcResponse msg) {
        RpcResponse rpcResponse = convertMsg(msg);
        DefaultPromise<RpcResponse> promise = ProviderServerHandler.promiseHolder.get().remove(msg.getRequestId());
        if (promise != null) {
            promise.trySuccess(rpcResponse);
        }
    }

    private RpcResponse convertMsg(DubboRpcResponse msg) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setResponseID(msg.getRequestId());
        rpcResponse.setResult(new String(msg.getBytes()));
        return rpcResponse;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("响应出错了......", cause);
        ctx.channel().close();
    }
}
