package top.wangjinhui.rpc.exception;

/**
 * @author Danny
 * @description 序列化异常
 * @CreateTime 2022/2/28 14:46
 */
public class SerializeExpection extends RuntimeException{
    public SerializeExpection(String msg) {
        super(msg);
    }
}
