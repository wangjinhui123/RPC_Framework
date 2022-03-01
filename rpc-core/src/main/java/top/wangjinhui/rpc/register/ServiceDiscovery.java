package top.wangjinhui.rpc.register;

import java.net.InetSocketAddress;

/**
 * @author Danny
 * @description 服务发现接口
 * @CreateTime 2022/3/1 12:25
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名称查找服务实体
     *
     * @param serviceName
     * @return
     */
    InetSocketAddress  lookupService(String serviceName);
}
