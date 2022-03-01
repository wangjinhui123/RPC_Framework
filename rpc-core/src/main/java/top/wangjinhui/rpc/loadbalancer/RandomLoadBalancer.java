package top.wangjinhui.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/1 10:58
 */
public class RandomLoadBalancer implements LoadBalancer{
    @Override
    public Instance select(List<Instance> instnces) {
        return instnces.get(new Random().nextInt(instnces.size()));
    }
}
