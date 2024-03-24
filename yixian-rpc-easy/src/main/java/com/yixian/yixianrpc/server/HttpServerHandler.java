package com.yixian.yixianrpc.server;

import com.yixian.yixianrpc.model.RpcRequest;
import com.yixian.yixianrpc.model.RpcResponse;
import com.yixian.yixianrpc.registry.LocalRegistry;
import com.yixian.yixianrpc.serializer.JDKSerializer;
import com.yixian.yixianrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * HTTP 请求
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    // 业务流程；
    // 1.反序列化请求为对象，拿到对象的参数
    // 2.根据服务名称从本地注册器中拿到对应的服务实现类
    // 3.通过反射执行方法，得到返回结果
    // 4.对结果进行封装并写入到响应中
    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer = new JDKSerializer();

        // 记录日志
        System.out.println("Received request:" + request.method() + " " + request.uri());
        // 异步处理 HTTP 请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                // 获取要调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                // 封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");

            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            } finally {
                doResponse(request, rpcResponse, serializer);
            }

        });
    }

    /**
     * 响应
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }


}
