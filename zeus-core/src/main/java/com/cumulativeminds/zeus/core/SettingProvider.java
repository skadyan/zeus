package com.cumulativeminds.zeus.core;

public interface SettingProvider {
    <T> T getSetting(Setting<T> setting);
}
