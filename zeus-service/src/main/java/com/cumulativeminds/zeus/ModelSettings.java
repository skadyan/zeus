package com.cumulativeminds.zeus;

import com.cumulativeminds.zeus.core.meta.Model;

public interface ModelSettings {
    <T> T getSetting(Model model, String context,String key, Class<T> type, T defaultValue);
    <T> T getSetting(Model model, String context, String key, Class<T> type);
}
