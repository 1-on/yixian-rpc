package com.yixian.example.comsumer;

import com.yixian.example.common.model.User;
import com.yixian.example.common.service.UserService;
import com.yixian.yixianrpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {
    public static void main(String[] args) {
        // todo 需要获取 UserService 的实现类对象
        // 静态代理
//        UserService userService = new UserServiceProxy();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//        UserService userService = null;
        User user = new User();
        user.setName("yixian");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user==null");
        }
    }
}
