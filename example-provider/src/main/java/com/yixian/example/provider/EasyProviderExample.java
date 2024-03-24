package com.yixian.example.provider;

import com.yixian.example.common.service.UserService;
import com.yixian.yixianrpc.registry.LocalRegistry;
import com.yixian.yixianrpc.server.HttpServer;
import com.yixian.yixianrpc.server.VertxHttpService;
import io.vertx.core.Vertx;

public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.registry(UserService.class.getName(), UserServiceImpl.class);
        // 启动 web 服务
        HttpServer httpServer = new VertxHttpService();
        httpServer.doStart(8080);
    }
}
