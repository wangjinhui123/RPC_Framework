package top.wangjinhui.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/1 10:56
 */
public interface LoadBalancer {

    Instance select(List<Instance> instnces);
}
