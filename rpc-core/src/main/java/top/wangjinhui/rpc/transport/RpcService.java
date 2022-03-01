package top.wangjinhui.rpc.transport;

import top.wangjinhui.rpc.serializer.CommonSerializer;

/**
 * @author Danny
 * @description 服务通用接口类
 * @CreateTime 2022/3/1 12:39
 */
public interface RpcService {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    <T> void publishService(T service, String serviceName);
}
