package com.fjf.example.provider;

import cn.hutool.core.net.NetUtil;
import com.fjf.example.common.service.UserService;
import com.fjf.fjfrpc.RpcApplication;
import com.fjf.fjfrpc.config.RegistryConfig;
import com.fjf.fjfrpc.config.RpcConfig;
import com.fjf.fjfrpc.model.ServiceMetaInfo;
import com.fjf.fjfrpc.registry.EtcdRegistry;
import com.fjf.fjfrpc.registry.LocalRegistry;
import com.fjf.fjfrpc.registry.Registry;
import com.fjf.fjfrpc.registry.RegistryFactory;
import com.fjf.fjfrpc.server.HttpServer;
import com.fjf.fjfrpc.server.VertxHttpServer;
import com.fjf.fjfrpc.server.tcp.VertxTcpServer;

/**
 * 服务提供者示例
 *
 */
public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 TCP 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(8080);
    }
}
