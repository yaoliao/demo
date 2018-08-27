package com.example.demo.exchange;

import com.example.demo.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/20 0020.
 */
public class Decode extends ByteToMessageDecoder {

    private static final Integer HEAD_LENGTH = 8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() < HEAD_LENGTH) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();

        //1:request 2:response
        int flag = byteBuf.readInt();

        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        Class<? extends Serializable> clazz = Integer.valueOf(1).equals(flag) ? RpcRequest.class : RpcResponse.class;
        Object o = SerializerFactory.load(1).deserialize(body, clazz);
        out.add(o);
    }
}
