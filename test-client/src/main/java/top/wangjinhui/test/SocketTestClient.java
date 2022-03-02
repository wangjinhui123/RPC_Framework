package top.wangjinhui.test;


import top.wangjinhui.rpc.api.ByeService;
import top.wangjinhui.rpc.api.HelloObject;
import top.wangjinhui.rpc.api.HelloService;
import top.wangjinhui.rpc.serializer.CommonSerializer;
import top.wangjinhui.rpc.transport.RpcClient;
import top.wangjinhui.rpc.transport.RpcClientProxy;
import top.wangjinhui.rpc.transport.socket.client.SocketClient;

import java.net.Socket;

/**
 * @author Danny
 * description 测试用消费者（客户端）
 * @CreateTime 2022/3/2 14:03
 */
public class SocketTestClient {

    public static void main(String[] args) {

        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println("Netty");
    }
}
