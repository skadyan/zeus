package com.cumulativeminds.zeus.core.meta;

import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;
import com.cumulativeminds.zeus.intergration.ChangeTrigger;
import com.cumulativeminds.zeus.intergration.ModelSourceIntegrationModel;

public class ModelDataSource {
    private final String type;
    private final TypedValueMapAccessor definition;
    private ModelSourceIntegrationModel integrationModel;
    private ChangeTrigger changeTrigger;

    public ModelDataSource(String type, TypedValueMapAccessor definition) {
        this.type = type;
        this.definition = definition;
    }

    public String getType() {
        return type;
    }

    public TypedValueMapAccessor getDefinition() {
        return definition;
    }

    public ModelSourceIntegrationModel getIntegrationModel() {
        return integrationModel;
    }

    public ChangeTrigger getChangeTrigger() {
        return changeTrigger;
    }

    protected void setIntegrationModel(ModelSourceIntegrationModel integrationModel) {
        this.integrationModel = integrationModel;
    }

    protected void setChangeTrigger(ChangeTrigger changeTrigger) {
        this.changeTrigger = changeTrigger;
    }

    protected void configure(Model owner) {
    }

}
