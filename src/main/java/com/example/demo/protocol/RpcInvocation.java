package com.example.demo.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class RpcInvocation implements Serializable {

    private static final long serialVersionUID = -6907437041710571375L;

    private String methodName;

    private String parameterTypes;

    private byte[] arguments;

    private Map<String, String> attachments;

    public RpcInvocation() {
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public byte[] getArguments() {
        return arguments;
    }

    public void setArguments(byte[] arguments) {
        this.arguments = arguments;
    }


    public void setAttachment(String key, String value) {
        if (attachments == null) {
            attachments = new HashMap<>();
        }
        attachments.put(key, value);
    }

    public String getAttachment(String key, String defaultValue) {
        if (attachments == null) {
            return defaultValue;
        }
        String value = attachments.get(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public String getAttachment(String key) {
        if (attachments == null) {
            return null;
        }
        return attachments.get(key);
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }
}
