package com.cumulativeminds.zeus.core;

public interface Setting<T> {
    String name();

    Class<T> getType();
}
