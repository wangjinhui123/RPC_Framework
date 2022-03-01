package top.wangjinhui.rpc.transport;

import top.wangjinhui.rpc.entity.RpcRequest;
import top.wangjinhui.rpc.serializer.CommonSerializer;

/**
 * @author Danny
 * @description 客户端通用接口
 * @CreateTime 2022/3/1 12:39
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
