package com.cumulativeminds.zeus.core;

public class WellknownSettings {

    public static enum S implements Setting<String> {
        ;

        @Override
        public Class<String> getType() {
            return String.class;
        }

    }

    public static enum B implements Setting<Boolean> {
        includeDeveloperMessage;

        @Override
        public Class<Boolean> getType() {
            return Boolean.class;
        }

    }

}
