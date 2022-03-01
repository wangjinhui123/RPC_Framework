package top.wangjinhui.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.enumeration.RpcError;
import top.wangjinhui.rpc.exception.RpcException;
import top.wangjinhui.rpc.util.NacosUtil;

import java.net.InetSocketAddress;

/**
 * @author Danny
 * @description Nacos 服务注册中心
 * @CreateTime 2022/3/1 12:25
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);


    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("服务注册时有错误发生：", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
