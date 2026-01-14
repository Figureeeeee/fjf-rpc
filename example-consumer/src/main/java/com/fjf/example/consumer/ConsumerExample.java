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
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
