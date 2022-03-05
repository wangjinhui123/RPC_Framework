package top.wangjinhui.test;

import top.wangjinhui.rpc.annotation.ServiceScan;
import top.wangjinhui.rpc.serializer.CommonSerializer;
import top.wangjinhui.rpc.transport.RpcService;
import top.wangjinhui.rpc.transport.netty.service.NettyServer;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/2 14:22
 */
@ServiceScan
public class NettyTestServer {

    public static void main(String[] args) {
        RpcService service = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        service.start();
    }
}
