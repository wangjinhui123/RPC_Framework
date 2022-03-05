package top.wangjinhui.test;

import top.wangjinhui.rpc.annotation.ServiceScan;
import top.wangjinhui.rpc.serializer.CommonSerializer;
import top.wangjinhui.rpc.transport.RpcService;
import top.wangjinhui.rpc.transport.socket.server.SocketServer;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/2 14:22
 */
@ServiceScan
public class SocketTestServer {

    public static void main(String[] args) {
        RpcService service = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        service.start();
    }
}
