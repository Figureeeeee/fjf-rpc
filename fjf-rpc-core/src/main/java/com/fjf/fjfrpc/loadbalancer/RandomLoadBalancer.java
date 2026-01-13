package com.fjf.fjfrpc.loadbalancer;

import com.fjf.fjfrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡器
 *
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }
        // 只有 1 个服务，不用随机
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        System.out.println("随机负载均衡器选择节点: " + serviceMetaInfoList.get(random.nextInt(size)).getServiceHost()
                + ":" + serviceMetaInfoList.get(random.nextInt(size)).getServicePort());
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
