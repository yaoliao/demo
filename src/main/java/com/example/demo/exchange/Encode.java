package com.example.demo.exchange;

import com.example.demo.serializer.Serializer;
import com.example.demo.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class Encode extends MessageToByteEncoder {


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        Integer flag = null;
        if (msg instanceof RpcRequest) {
            flag = 1;
        } else if (msg instanceof RpcResponse) {
            flag = 2;
        } else {
            throw new IllegalArgumentException("传的啥呀");
        }
        Serializer serializer = SerializerFactory.load(1);
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeInt(flag);
        out.writeBytes(bytes);
    }
}
