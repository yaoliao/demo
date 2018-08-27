package com.example.demo.exchange;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Administrator
 * @date 2018/8/20 0020
 */
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -7632194328922814010L;

    private Long requestID;

    private String interfaceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Map<String, String> attachments;

    // test -------
    private String parameterTypesStr;
    // test -------
    private String argumentStr;

    public RpcRequest() {
    }

    private RpcRequest(Builder builder) {
        setRequestID(builder.requestID);
        setInterfaceName(builder.interfaceName);
        setMethodName(builder.methodName);
        setParameterTypes(builder.parameterTypes);
        setArguments(builder.arguments);
        setAttachments(builder.attachments);
        setParameterTypesStr(builder.parameterTypesStr);
        setArgumentStr(builder.argumentStr);
    }

    public String getArgumentStr() {
        return argumentStr;
    }

    public void setArgumentStr(String argumentStr) {
        this.argumentStr = argumentStr;
    }

    public String getParameterTypesStr() {
        return parameterTypesStr;
    }

    public void setParameterTypesStr(String parameterTypesStr) {
        this.parameterTypesStr = parameterTypesStr;
    }

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }


    public static final class Builder {
        private Long requestID;
        private String interfaceName;
        private String methodName;
        private Class<?>[] parameterTypes;
        private Object[] arguments;
        private Map<String, String> attachments;
        private String parameterTypesStr;
        private String argumentStr;

        public Builder() {
        }

        public Builder requestID(Long val) {
            requestID = val;
            return this;
        }

        public Builder interfaceName(String val) {
            interfaceName = val;
            return this;
        }

        public Builder methodName(String val) {
            methodName = val;
            return this;
        }

        public Builder parameterTypes(Class<?>[] val) {
            parameterTypes = val;
            return this;
        }

        public Builder arguments(Object[] val) {
            arguments = val;
            return this;
        }

        public Builder attachments(Map<String, String> val) {
            attachments = val;
            return this;
        }

        public Builder parameterTypesStr(String val) {
            parameterTypesStr = val;
            return this;
        }

        public Builder argumentStr(String val) {
            argumentStr = val;
            return this;
        }

        public RpcRequest build() {
            return new RpcRequest(this);
        }
    }
}
