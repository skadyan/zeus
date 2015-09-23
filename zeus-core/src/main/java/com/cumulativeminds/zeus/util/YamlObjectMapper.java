package com.cumulativeminds.zeus.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 7757821229340934280L;

    public YamlObjectMapper() {
        super(new YAMLFactory());
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setSerializationInclusion(Include.NON_NULL);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> map(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return readValue(is, Map.class);
        }
    }
}
