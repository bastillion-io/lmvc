package com.lmvc.annotation;

import java.util.Arrays;
import java.util.List;

public enum MethodType {
    GET("GET"), POST("POST");

    private String method;

    MethodType(String method) {
        this.method = method;
    }

    public String toString() {
        return method.toString();
    }

}