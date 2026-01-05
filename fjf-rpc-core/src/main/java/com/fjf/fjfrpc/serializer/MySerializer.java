package com.fjf.fjfrpc.serializer;

import java.io.IOException;

/**
 * 自定义序列化器（这里简单复用 JDK 序列化，仅做测试）
 */
public class MySerializer implements Serializer {

    private final JdkSerializer jdkSerializer = new JdkSerializer();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        System.out.println("=== 自定义序列化器被调用了 ===");
        return jdkSerializer.serialize(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        System.out.println("=== 自定义反序列化器被调用了 ===");
        return jdkSerializer.deserialize(bytes, type);
    }
}
