package top.wangjinhui.rpc.transport.socket.server;

import top.wangjinhui.rpc.factory.ThreadPollFactory;
import top.wangjinhui.rpc.handler.RequestHandler;
import top.wangjinhui.rpc.hook.ShutdownHook;
import top.wangjinhui.rpc.provider.ServiceProviderImpl;
import top.wangjinhui.rpc.register.NacosServiceRegistry;
import top.wangjinhui.rpc.serializer.CommonSerializer;
import top.wangjinhui.rpc.transport.AbstractRpcServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author Danny
 * @description Socket 方式远程方法调用的提供者（服务器）
 * @CreateTime 2022/3/1 14:07
 */
public class SocketServer extends AbstractRpcServer {

    private final ExecutorService threadPool;
    private final CommonSerializer serializer;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPollFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host,port));
            logger.info("服务启动...");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接：{}：{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务启动时有错误发送：", e);
        }
    }


}
