package com.cumulativeminds.zeus.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 7757821229340934280L;

    public YamlObjectMapper() {
        super(new YAMLFactory());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> map(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return readValue(is, Map.class);
        }
    }
}
