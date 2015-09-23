package com.cumulativeminds.zeus.api.controller;

import java.util.LinkedHashMap;

import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class SimpleStatus extends LinkedHashMap<String, Object> {

    public static SimpleStatus status(String status) {
        SimpleStatus st = new SimpleStatus();
        st.put("status", status);
        return st;
    }

    public SimpleStatus with(String key, Object value) {
        put(key, value);
        return this;
    }

    public Response build() {
        return Response.ok(this).build();
    }

}
