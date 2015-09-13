package com.cumulativeminds.zeus.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

public class JsonObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = -5764414144531261986L;

    public JsonObjectMapper() {
        registerModule(new JSR310Module());
        configure();
    }

    private void configure() {
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, Boolean.FALSE);
    }

}
