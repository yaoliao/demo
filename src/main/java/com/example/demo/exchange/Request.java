package com.example.demo.exchange;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/16 0016.
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 8651437686984994126L;

    private Integer requestID;
    private String interfaceName;
    private String method;
    private String[] parameterTypes;
    private Object[] parameters;


    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
