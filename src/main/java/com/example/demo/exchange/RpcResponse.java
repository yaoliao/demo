package com.example.demo.exchange;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 2530053382861823389L;

    public static final byte OK = 20;
    public static final byte ERROR = 30;

    private byte status = OK;

    private Long responseID;

    private String errorMsg;

    private Object result;

    public RpcResponse() {
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Long getResponseID() {
        return responseID;
    }

    public void setResponseID(Long responseID) {
        this.responseID = responseID;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
