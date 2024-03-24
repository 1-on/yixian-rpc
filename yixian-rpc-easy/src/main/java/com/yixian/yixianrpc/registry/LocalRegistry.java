package com.yixian.yixianrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心
 */
public class LocalRegistry {

    /**
     * 注册信息存储
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 服务注册
     *
     * @param serviceName
     * @param implClass
     */
    public static void registry(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务
     *
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    private static void remove(String serviceName) {
        map.remove(serviceName);
    }


}
