package com.fjf.example.provider;

import com.fjf.example.common.service.UserService;
import com.fjf.fjfrpc.RpcApplication;
import com.fjf.fjfrpc.registry.LocalRegistry;
import com.fjf.fjfrpc.server.HttpServer;
import com.fjf.fjfrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 *
 */
public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
