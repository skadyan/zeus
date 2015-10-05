package com.cumulativeminds.zeus.intergration;

public enum ChangeTriggerType {
    multipart, raw, urlencoded;

    public static ChangeTriggerType fromText(String text) {
        for (ChangeTriggerType v : values()) {
            if (v.name().equals(text)) {
                return v;
            }
        }
        return null;
    }
}
