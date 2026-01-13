package com.fjf.example.consumer;

import com.fjf.example.common.model.User;
import com.fjf.example.common.service.UserService;
import com.fjf.fjfrpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 *
 */
public class ConsumerExample {

    public static void main(String[] args) {
        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("fjf");
        // 循环调用6次，观察每次选中的节点
        for (int i = 0; i < 6; i++) {
            User newUser = userService.getUser(user);
            if (newUser != null) {
                System.out.println("第" + (i+1) + "次调用结果: " + newUser.getName());
            } else {
                System.out.println("第" + (i+1) + "次调用: user == null");
            }
        }
    }
}
