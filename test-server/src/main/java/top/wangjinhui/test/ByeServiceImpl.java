package top.wangjinhui.test;

import top.wangjinhui.rpc.api.ByeService;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/2 14:21
 */
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}
