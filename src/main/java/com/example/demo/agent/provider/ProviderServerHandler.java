package com.example.demo.agent.provider;

import com.example.demo.exchange.RpcRequest;
import com.example.demo.exchange.RpcResponse;
import com.example.demo.protocol.DubboRpcRequest;
import com.example.demo.protocol.RpcInvocation;
import com.example.demo.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.FastThreadLocal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class ProviderServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static FastThreadLocal<LongObjectHashMap<DefaultPromise<RpcResponse>>> promiseHolder =
            new FastThreadLocal<LongObjectHashMap<DefaultPromise<RpcResponse>>>() {
                @Override
                protected LongObjectHashMap<DefaultPromise<RpcResponse>> initialValue() throws Exception {
                    return new LongObjectHashMap<>();
                }
            };

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ProviderClient client = ProviderClient.get(ctx.channel().eventLoop().toString());
        if (client == null) {
            ProviderClient providerClient = new ProviderClient(ctx.channel().eventLoop());
            providerClient.init();
            ProviderClient.put(ctx.channel().eventLoop().toString(), providerClient);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        DefaultPromise<RpcResponse> promise = new DefaultPromise<>(ctx.executor());
        promise.addListener(future -> {
            RpcResponse rpcResponse = (RpcResponse) future.get();
            ctx.writeAndFlush(rpcResponse);
        });
        promiseHolder.get().put(msg.getRequestID(), promise);
        ProviderClient.get(ctx.channel().eventLoop().toString()).getChannel().writeAndFlush(convertMsg(msg));
    }

    private DubboRpcRequest convertMsg(RpcRequest agentRequest) {
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName(agentRequest.getMethodName());
        invocation.setAttachment("path", agentRequest.getInterfaceName());
        invocation.setParameterTypes(agentRequest.getParameterTypesStr());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        try {
            JsonUtils.writeObject(agentRequest.getArgumentStr(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        invocation.setArguments(out.toByteArray());

        DubboRpcRequest dubboRpcRequest = new DubboRpcRequest();
        dubboRpcRequest.setId(agentRequest.getRequestID());
        dubboRpcRequest.setVersion("2.0.0");  // TODO 版本号
        dubboRpcRequest.setData(invocation);
        return dubboRpcRequest;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("================");
        ctx.channel().close();
    }
}
