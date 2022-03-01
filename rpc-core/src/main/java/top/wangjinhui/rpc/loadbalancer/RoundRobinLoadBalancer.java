package top.wangjinhui.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/1 11:00
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private int index = 0;

    @Override
    public Instance select(List<Instance> instnces) {
        if (index >= instnces.size()) {
            index %= instnces.size();
        }
        return instnces.get(index++);
    }
}
