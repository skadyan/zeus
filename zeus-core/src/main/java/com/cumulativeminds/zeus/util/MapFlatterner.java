package com.cumulativeminds.zeus.util;

import java.util.Map;

import org.springframework.beans.factory.config.YamlProcessor;

public class MapFlatterner extends YamlProcessor {
    private static final MapFlatterner INSTANCE = new MapFlatterner();

    public static Map<String, Object> asFlatternedMap(Map<String, Object> source) {
        return INSTANCE.getFlattenedMap(source);
    }
}
