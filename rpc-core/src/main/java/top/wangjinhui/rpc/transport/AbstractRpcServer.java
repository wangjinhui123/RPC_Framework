package top.wangjinhui.rpc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.annotation.Service;
import top.wangjinhui.rpc.annotation.ServiceScan;
import top.wangjinhui.rpc.enumeration.RpcError;
import top.wangjinhui.rpc.exception.RpcException;
import top.wangjinhui.rpc.provider.ServiceProvider;
import top.wangjinhui.rpc.register.ServiceRegistry;
import top.wangjinhui.rpc.util.ReflectUtil;

import java.util.Set;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/1 12:39
 */
public abstract class AbstractRpcServer implements RpcService{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    public void scanService() {
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(Service.class)) {
                logger.error("启动类缺少 @ServiceScan 注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            logger.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("创建" + ":" + clazz + "时有错误发生");
                    continue;
                }
                if ("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface : interfaces) {
                        publishService(obj, oneInterface.getCanonicalName());
                    }
                } else  {
                    publishService(obj, serviceName);
                }
            }
        }
    }
}
