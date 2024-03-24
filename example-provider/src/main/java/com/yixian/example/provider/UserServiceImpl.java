package com.yixian.example.provider;

import com.yixian.example.common.model.User;
import com.yixian.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名:" + user.getName());
        return user;
    }
}
