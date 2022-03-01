package top.wangjinhui.rpc.transport.netty.client;

import top.wangjinhui.rpc.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/1 14:04
 */
public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>> unprocessedRequestFutures = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        unprocessedRequestFutures.put(requestId, future);
    }

    public void remove(String requestId) {unprocessedRequestFutures.remove(requestId);}


    public void complete(RpcResponse rpcResponse) throws IllegalAccessException {
        CompletableFuture<RpcResponse> future = unprocessedRequestFutures.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalAccessException();
        }
    }
}
