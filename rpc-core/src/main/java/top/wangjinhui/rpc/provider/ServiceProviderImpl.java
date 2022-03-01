package top.wangjinhui.rpc.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.enumeration.RpcError;
import top.wangjinhui.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Danny
 * @description 默认的服务注册表，保存服务端本地服务
 * @CreateTime 2022/3/1 10:27
 */
public class ServiceProviderImpl implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public <T> void addSeriveProvider(T serivce, String seriveName) {
        if (registeredService.contains(seriveName)) return;
        registeredService.add(seriveName);
        serviceMap.put(seriveName, serivce);
        logger.info("向接口：{} 注册服务：{}", serivce.getClass().getInterfaces(), seriveName);
    }

    @Override
    public Object getSeriveProvider(String seriveName) {
        Object serivce = serviceMap.get(seriveName);
        if (serivce == null) {
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        return serivce;
    }
}
