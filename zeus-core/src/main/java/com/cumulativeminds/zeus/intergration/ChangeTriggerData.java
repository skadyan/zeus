package com.cumulativeminds.zeus.intergration;

import java.util.List;

public class ChangeTriggerData {
    public static class Property {
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private List<Property> items;

    public List<Property> items() {
        return items;
    }
}
