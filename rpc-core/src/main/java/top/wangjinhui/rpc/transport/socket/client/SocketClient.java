package top.wangjinhui.rpc.transport.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.entity.RpcRequest;
import top.wangjinhui.rpc.entity.RpcResponse;
import top.wangjinhui.rpc.enumeration.ResponseCode;
import top.wangjinhui.rpc.enumeration.RpcError;
import top.wangjinhui.rpc.exception.RpcException;
import top.wangjinhui.rpc.loadbalancer.LoadBalancer;
import top.wangjinhui.rpc.loadbalancer.RandomLoadBalancer;
import top.wangjinhui.rpc.register.NacosServiceDiscovery;
import top.wangjinhui.rpc.register.ServiceDiscovery;
import top.wangjinhui.rpc.serializer.CommonSerializer;
import top.wangjinhui.rpc.transport.RpcClient;
import top.wangjinhui.rpc.transport.socket.util.ObjectReader;
import top.wangjinhui.rpc.transport.socket.util.ObjectWriter;
import top.wangjinhui.rpc.util.RpcMessageChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Danny
 * @description Socket 方式原创方法调用的消费者（客户端）
 * @CreateTime 2022/3/1 14:06
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;

    private final CommonSerializer serializer;

    public SocketClient(Integer kryoSerializer) {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }

    public SocketClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public SocketClient(Integer serializer, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置初始化序列器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if (rpcResponse == null) {
                logger.error("服务调用失败， service:{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service:" +rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("服务调用失败， service:{}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service:" + rpcRequest.getInterfaceName());
            }
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse;
        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败：", e);
        }


    }
}
