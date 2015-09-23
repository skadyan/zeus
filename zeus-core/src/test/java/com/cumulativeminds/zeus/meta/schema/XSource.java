package com.cumulativeminds.zeus.meta.schema;

public class XSource {
    private String type;

    private XIntegrationModel model;

    private XChangeTrigger trigger;

    public XSource() {
    }

    public XIntegrationModel getModel() {
        return model;
    }

    public XChangeTrigger getTrigger() {
        return trigger;
    }

    public String getType() {
        return type;
    }

    public void setTrigger(XChangeTrigger trigger) {
        this.trigger = trigger;
    }

    public void setModel(XIntegrationModel model) {
        this.model = model;
    }

    public void setType(String type) {
        this.type = type;
    }
}
