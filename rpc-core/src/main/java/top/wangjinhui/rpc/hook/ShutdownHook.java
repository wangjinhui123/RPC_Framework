package top.wangjinhui.rpc.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.factory.ThreadPollFactory;
import top.wangjinhui.rpc.util.NacosUtil;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/1 10:48
 */
public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPollFactory.shutDownAll();
        }));
    }
}
