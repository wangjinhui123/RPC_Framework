package top.wangjinhui.test;

import top.wangjinhui.rpc.api.ByeService;
import top.wangjinhui.rpc.api.HelloObject;
import top.wangjinhui.rpc.api.HelloService;
import top.wangjinhui.rpc.serializer.CommonSerializer;
import top.wangjinhui.rpc.transport.RpcClient;
import top.wangjinhui.rpc.transport.RpcClientProxy;
import top.wangjinhui.rpc.transport.netty.client.NettyClient;

import java.io.ByteArrayOutputStream;

/**
 * @author Danny
 * @description  测试用Netty消费者
 * @CreateTime 2022/3/2 14:03
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a meaage");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));

    }
}
