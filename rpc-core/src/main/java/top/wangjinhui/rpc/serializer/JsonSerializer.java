package top.wangjinhui.rpc.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.wangjinhui.rpc.entity.RpcRequest;
import top.wangjinhui.rpc.enumeration.SerializerCode;
import top.wangjinhui.rpc.exception.SerializeExpection;

/**
 * @author Danny
 * @description 使用JSON格式的序列化器
 * @CreateTime 2022/2/28 21:05
 */
public class JsonSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeExpection("序列化时有错误发生!");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if (obj instanceof RpcRequest) {
                obj = handleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeExpection("序列化时有错误发生");
        }
    }

    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if (!clazz.isAssignableFrom(rpcRequest.getParamTypes()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParamTypes()[i]);
                rpcRequest.getParamTypes()[i] = (Class<?>) objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
