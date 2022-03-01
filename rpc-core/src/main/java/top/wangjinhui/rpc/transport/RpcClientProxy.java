package top.wangjinhui.rpc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.entity.RpcRequest;
import top.wangjinhui.rpc.entity.RpcResponse;
import top.wangjinhui.rpc.transport.netty.client.NettyClient;
import top.wangjinhui.rpc.util.RpcMessageChecker;

import javax.sound.sampled.Port;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author Danny
 * @description RPC 客户端动态代理
 * @CreateTime 2022/3/1 12:39
 */
public class RpcClientProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用方法：{}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes(), false);
        RpcResponse rpcResponse = null;
        if (client instanceof NettyClient) {
            rpcResponse = (RpcResponse) client.sendRequest(rpcRequest);
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
