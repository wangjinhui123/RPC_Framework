package top.wangjinhui.rpc.transport.socket.util;

import top.wangjinhui.rpc.entity.RpcRequest;
import top.wangjinhui.rpc.enumeration.PackageType;
import top.wangjinhui.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Danny
 * @description
 * @CreateTime 2022/3/1 14:07
 */
public class ObjectWriter {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream outputStream, Object obj, CommonSerializer serializer) throws IOException {

        outputStream.write(intoBytes(MAGIC_NUMBER));
        if (obj instanceof RpcRequest) {
            outputStream.write(intoBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intoBytes(PackageType.RESPONSE_PACK.getCode()));
        }
        outputStream.write(intoBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(obj);
        outputStream.write(intoBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();


    }

    private static byte[] intoBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
}
