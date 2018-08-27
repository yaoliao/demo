package com.example.demo.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.example.demo.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class KryoSerializer implements Serializer {
    @Override
    public byte[] serialize(Object o) {
        Kryo kryo = new Kryo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, o);
        output.flush();
        output.close();
        byte[] bytes = outputStream.toByteArray();
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Kryo kryo = new Kryo();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(inputStream);
        T resule = kryo.readObject(input, clazz);
        input.close();
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resule;
    }
}
