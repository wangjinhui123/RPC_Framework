package top.wangjinhui.rpc.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.entity.RpcRequest;
import top.wangjinhui.rpc.entity.RpcResponse;
import top.wangjinhui.rpc.enumeration.ResponseCode;
import top.wangjinhui.rpc.provider.ServiceProvider;
import top.wangjinhui.rpc.provider.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Danny
 * @description 进行过程调用的处理器
 * @CreateTime 2022/3/1 10:21
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getSeriveProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;

        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("服务：{} 成功调用方法：{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return  result;
    }

}
