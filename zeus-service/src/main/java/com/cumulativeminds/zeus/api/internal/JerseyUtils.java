package com.cumulativeminds.zeus.api.internal;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class JerseyUtils {

    public static MultivaluedMap<String, String> getQueryParametes(UriInfo uriInfo) {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        uriInfo.getQueryParameters().forEach((k, v) -> parameters.addAll(k, v));
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

}
