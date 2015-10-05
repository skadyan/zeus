package com.cumulativeminds.zeus.intergration;

import java.util.Map;

public class ChangeTrigger {
    public static class ArgType {
        private Class<?> type;
        private String format;
        private boolean required;

        public ArgType(Class<?> type, String format, boolean required) {
            this.type = type;
            this.format = format;
            this.required = required;
        }

        public String getFormat() {
            return format;
        }

        public Class<?> getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }
    }

    private Map<String, ArgType> arguments;

    private ChangeTriggerType type;

    public ChangeTrigger(ChangeTriggerType type, Map<String, ArgType> arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public ChangeTriggerType getType() {
        return type;
    }

    public Map<String, ArgType> getArguments() {
        return arguments;
    }
}
