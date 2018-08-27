package com.example.demo.serializer;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public interface Serializer {

    byte[] serialize(Object o);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
