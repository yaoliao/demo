package com.example.demo.protocol;

/**
 * Created by Administrator on 2018/8/20 0020.
 */
public class DubboRpcResponse {
    private long requestId;
    private byte[] bytes;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

}
