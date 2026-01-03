package com.fjf.example.provider;

import com.fjf.example.common.service.UserService;
import com.fjf.fjfrpc.registry.LocalRegistry;
import com.fjf.fjfrpc.server.HttpServer;
import com.fjf.fjfrpc.server.VertxHttpServer;

public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
