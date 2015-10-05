package com.cumulativeminds.zeus.api.controller;

import java.util.LinkedHashMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

@SuppressWarnings("serial")
public class SimpleStatus extends LinkedHashMap<String, Object> {

    public static SimpleStatus status(String status) {
        SimpleStatus st = new SimpleStatus();
        st.put("message", status);
        return st;
    }

    public SimpleStatus with(String key, Object value) {
        put(key, value);
        return this;
    }

    public Response build() {
        put("status", Status.OK.getStatusCode());
        return Response.ok(this).build();
    }

    public Response build(StatusType statusType) {
        put("status", statusType.getStatusCode());
        return Response.status(statusType).entity(this).build();
    }
}
