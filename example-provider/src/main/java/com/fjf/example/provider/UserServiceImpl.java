package com.fjf.example.provider;

import com.fjf.example.common.model.User;
import com.fjf.example.common.service.UserService;

public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
