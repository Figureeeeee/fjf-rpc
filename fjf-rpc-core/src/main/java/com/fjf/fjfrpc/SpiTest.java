package com.fjf.fjfrpc;

import com.fjf.fjfrpc.serializer.Serializer;
import com.fjf.fjfrpc.serializer.SerializerFactory;
import com.fjf.fjfrpc.spi.SpiLoader;

import java.util.Map;

public class SpiTest {
    public static void main(String[] args) {
        // 1. 加载 Serializer 的 SPI
        Map<String, Class<?>> keyClassMap = SpiLoader.load(Serializer.class);
        System.out.println("=== 加载到的所有序列化器 ===");
        for (Map.Entry<String, Class<?>> entry : keyClassMap.entrySet()) {
            System.out.println("key=" + entry.getKey() + " => " + entry.getValue().getName());
        }

        // 2. 测试正常 key（存在）
        System.out.println("\n=== 测试正常 key: jdk ===");
        try {
            Serializer jdk = SerializerFactory.getInstance("jdk");
            System.out.println("成功获取: " + jdk.getClass().getName());
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }

        // 3. 测试异常 key（不存在）
        System.out.println("\n=== 测试异常 key: xxx ===");
        try {
            Serializer xxx = SerializerFactory.getInstance("xxx");
            System.out.println("成功获取: " + xxx.getClass().getName());
        } catch (Exception e) {
            System.out.println("预期内失败: " + e.getMessage());
        }
    }
}
