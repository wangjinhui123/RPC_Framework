package top.wangjinhui.rpc.register;

import java.net.InetSocketAddress;

/**
 * @author Danny
 * @description 服务注册接口
 * @CreateTime 2022/3/1 12:26
 */
public interface ServiceRegistry {

    /**
     * 将一个服务注册进注册表
     *
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
