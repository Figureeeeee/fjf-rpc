package com.fjf.fjfrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.fjf.fjfrpc.RpcApplication;
import com.fjf.fjfrpc.config.RpcConfig;
import com.fjf.fjfrpc.constant.RpcConstant;
import com.fjf.fjfrpc.fault.retry.RetryStrategy;
import com.fjf.fjfrpc.fault.retry.RetryStrategyFactory;
import com.fjf.fjfrpc.loadbalancer.LoadBalancer;
import com.fjf.fjfrpc.loadbalancer.LoadBalancerFactory;
import com.fjf.fjfrpc.model.RpcRequest;
import com.fjf.fjfrpc.model.RpcResponse;
import com.fjf.fjfrpc.model.ServiceMetaInfo;
import com.fjf.fjfrpc.protocol.*;
import com.fjf.fjfrpc.registry.Registry;
import com.fjf.fjfrpc.registry.RegistryFactory;
import com.fjf.fjfrpc.serializer.Serializer;
import com.fjf.fjfrpc.serializer.SerializerFactory;
import com.fjf.fjfrpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理（JDK 动态代理）
 *
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            // 默认选择第一个服务提供者（过时，改为负载均衡选择）
//            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", method.getName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            System.out.println("ServiceProxy 负载均衡器选中节点: " + selectedServiceMetaInfo.getServiceHost()
                    + ":" + selectedServiceMetaInfo.getServicePort());

            // rpc请求
            // 使用重试策略
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}
