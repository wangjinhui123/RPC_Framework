package top.wangjinhui.rpc.exception;

import top.wangjinhui.rpc.enumeration.RpcError;

/**
 * @author Danny
 * @description RPC 调用异常
 * @CreateTime 2022/2/28 14:39
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
