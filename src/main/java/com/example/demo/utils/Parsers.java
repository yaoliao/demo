package com.example.demo.utils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/16 0016.
 */
public class Parsers {

    private Parsers() {

    }

    public static Map<String, String> fastParser(FullHttpRequest fullHttpRequest) {
        String request = fullHttpRequest.content().toString(StandardCharsets.UTF_8);
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request, StandardCharsets.UTF_8, false);
        Map<String, List<String>> qParameters = queryStringDecoder.parameters();
        String interfaceName = qParameters.get("interface").get(0);
        String method = qParameters.get("method").get(0);
        String parameterTypes = qParameters.get("parameterTypes").get(0);
        String parameters = qParameters.get("parameters").get(0);
        Map<String, String> map = new HashMap<>(8);
        map.put("interface", interfaceName);
        map.put("method", method);
        map.put("parameterTypes", parameterTypes);
        map.put("parameters", parameters);
        return map;
    }

}
