package com.example.demo.agent.consumer;

import com.example.demo.exchange.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class ConsumerClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private Logger logger = LoggerFactory.getLogger(ConsumerClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {

        Promise promise = ConsumerServerHandler.PROMISE_HOLDER.remove(msg.getResponseID());
        if (promise != null) {
            promise.trySuccess(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("ConsumerClientHandler出现异常", cause);
        ctx.channel().close();
    }
}
