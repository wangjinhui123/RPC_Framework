package top.wangjinhui.rpc.provider;

/**
 * @author Danny
 * @description 保存和提供服务实例对象
 * @CreateTime 2022/3/1 10:25
 */
public interface ServiceProvider {

    <T> void addSeriveProvider(T serive, String seriveName);

    Object getSeriveProvider(String seriveName);

}
