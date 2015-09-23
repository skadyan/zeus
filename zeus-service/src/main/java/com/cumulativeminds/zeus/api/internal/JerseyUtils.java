package com.cumulativeminds.zeus.api.internal;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class JerseyUtils {

    public static MultivaluedMap<String, Object> getQueryParametes(UriInfo uriInfo) {
        MultivaluedMap<String, Object> parameters = new MultivaluedHashMap<>();
        uriInfo.getQueryParameters().forEach((k, v) -> parameters.add(k, v));
        return parameters;

    }

    public static MultivaluedMap<String, String> mergeParameters(UriInfo uriInfo, Form form) {
        MultivaluedMap<String, String> map1 = uriInfo.getQueryParameters();
        MultivaluedMap<String, String> map2 = form.asMap();

        return Stream.of(map1, map2)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(
                        toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                JerseyUtils::mergeList,
                                MultivaluedHashMap::new));
    }

    private static <T> List<T> mergeList(List<T> l1, List<T> l2) {
        List<T> list = new ArrayList<>(l1);
        list.addAll(l2);

        return list;
    }

    public static Map<String, Object> mapParametersToObject(MultivaluedMap<String, String> parameters,
            Set<String> fields) {
        Map<String, Object> currentObject = new HashMap<>();
        for (String field : fields) {
            String value = parameters.getFirst(field);
            currentObject.put(field, value);
        }

        return currentObject;
    }

}
