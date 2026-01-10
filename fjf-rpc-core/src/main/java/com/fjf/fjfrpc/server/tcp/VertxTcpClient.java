package com.fjf.fjfrpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public class VertxTcpClient {

    public void start() {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

//        // 演示半包、粘包问题
//        vertx.createNetClient().connect(8888, "localhost", result -> {
//            if (result.succeeded()) {
//                System.out.println("Connected to TCP server");
//                io.vertx.core.net.NetSocket socket = result.result();
////                // 发送数据
////                socket.write("Hello, server!");
//                for (int i = 0; i < 1000; i++) {
//                    // 发送数据
//                    socket.write("Hello, server!Hello, server!Hello, server!Hello, server!");
//                }
//                // 接收响应
//                socket.handler(buffer -> {
//                    System.out.println("Received response from server: " + buffer.toString());
//                });
//            } else {
//                System.err.println("Failed to connect to TCP server");
//            }
//        });
        vertx.createNetClient().connect(8888, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Connected to TCP server");
                io.vertx.core.net.NetSocket socket = result.result();
                for (int i = 0; i < 1000; i++) {
                    // 发送数据
                    Buffer buffer = Buffer.buffer();
                    String str = "Hello, server!Hello, server!Hello, server!Hello, server!";
                    buffer.appendInt(0);
                    buffer.appendInt(str.getBytes().length);
                    buffer.appendBytes(str.getBytes());
                    socket.write(buffer);
                }
                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            } else {
                System.err.println("Failed to connect to TCP server");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
