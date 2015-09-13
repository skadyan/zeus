package com.cumulativeminds.zeus.impl.yaml;

import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cumulativeminds.zeus.core.Keyword;
import com.cumulativeminds.zeus.core.meta.Exceptions;
import com.cumulativeminds.zeus.core.meta.IllegalModelException;
import com.cumulativeminds.zeus.core.meta.K;

public class TypedValueMapAccessor {
    private final Map<String, Object> map;
    private final URL source;

    public TypedValueMapAccessor(URL source, Map<String, Object> map) {
        requireNonNull(map, "cannot access value from null map");
        this.map = map;
        this.source = source;
    }

    public String getSimpleValue(Keyword key) {
        return get(key, String.class);
    }

    public Integer getIntValue(Keyword key) {
        return get(key, Integer.class);
    }

    public Long getLongValue(Keyword key) {
        return get(key, Long.class);
    }

    public <T> T get(Keyword key, Class<? extends T> type) {
        return type.cast(map.get(key.name()));
    }

    public TypedValueMapAccessor getNestedObject(Keyword key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> defs = get(key, Map.class);
        return defs == null ? null : new TypedValueMapAccessor(source, defs);
    }

    public TypedValueMapAccessor getNestedObject(String key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> defs = Map.class.cast(map.get(key));

        return new TypedValueMapAccessor(source, defs);
    }

    public List<TypedValueMapAccessor> getList(K key) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> defs = get(key, List.class);
        List<TypedValueMapAccessor> list = null;
        if (defs != null) {
            list = defs.stream()
                    .map(e -> new TypedValueMapAccessor(source, e))
                    .collect(Collectors.toList());
        }
        return list;
    }

    public String ensureAndGetSingleKey() {
        if (map.size() != 1) {
            throw new IllegalModelException(Exceptions.SINGLETON_OBJECT, map);
        }
        return map.keySet().iterator().next();
    }

    public boolean getBooleanValue(K key) {
        Object obj = (Object) map.get(key.name());
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        String text = String.valueOf(obj);
        boolean value = false;
        if (text != null) {
            value = text.equalsIgnoreCase("yes") ||
                    text.equalsIgnoreCase("true") ||
                    text.equalsIgnoreCase("1") ||
                    text.equalsIgnoreCase("on");
        }

        return value;
    }

    public boolean containsKey(K key) {
        return map.containsKey(key.name());
    }

    public String getSource() {
        return source.toString();
    }

    public TypedValueMapAccessor getNestedObject(Keyword key, String defaultKeyOfSingleValue) {
        Object o = get(key, Object.class);
        TypedValueMapAccessor value = null;
        if (o != null) {
            value = (o instanceof Map) ? getNestedObject(key)
                    : new TypedValueMapAccessor(source, singletonMap(defaultKeyOfSingleValue, o));
        }
        return value;
    }

    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(map);
    }
}
