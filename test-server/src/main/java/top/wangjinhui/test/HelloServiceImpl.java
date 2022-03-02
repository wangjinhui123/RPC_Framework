package top.wangjinhui.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.api.HelloObject;
import top.wangjinhui.rpc.api.HelloService;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/2 14:21
 */
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接受到消息： {}", object.getMessage());
        return "这是Impl1方法";
    }
}
