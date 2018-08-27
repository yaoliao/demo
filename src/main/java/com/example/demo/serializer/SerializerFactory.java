package com.example.demo.serializer;

import com.example.demo.serializer.kryo.KryoSerializer;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class SerializerFactory {

    public static Serializer load(Integer type) {
        SerializerEnum serializerEnum = SerializerEnum.valueOf(type);
        return Optional.ofNullable(serializerEnum).map(SerializerEnum::getSerializer)
                .orElse(SerializerEnum.KRYO.serializer);
    }

    enum SerializerEnum {

        /**
         * Kryo序列化方式
         */
        KRYO(1, new KryoSerializer());

        private Integer type;

        private Serializer serializer;


        SerializerEnum(Integer type, Serializer serializer) {
            this.type = type;
            this.serializer = serializer;
        }

        static SerializerEnum valueOf(Integer type) {
            SerializerEnum[] values = SerializerEnum.values();
            return Arrays.stream(values).filter(e -> e.type.equals(type)).findFirst().orElse(null);
        }


        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Serializer getSerializer() {
            return serializer;
        }

        public void setSerializer(Serializer serializer) {
            this.serializer = serializer;
        }
    }

}
