package com.cumulativeminds.zeus.core.meta;

import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

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

    @PostConstruct
    protected void parse() {
        TypedValueMapAccessor definition = getDefinition();

        parseIntegegrationModel(definition);
        parseChangeTrigger(definition);
    }

    private void parseChangeTrigger(TypedValueMapAccessor definition) {
        TypedValueMapAccessor triggerDefinition = definition.getNestedObject(K.changeTrigger);
        if (getIntegrationModel() == ModelSourceIntegrationModel.PULL) {
            if (triggerDefinition == null) {
                throw new IllegalModelException("Trigger definition must be provided for PULL model in "
                        + definition.getSource());
            }
            String type = triggerDefinition.getSimpleValue(K.type);
            @SuppressWarnings("unchecked")
            List<String> filterable = triggerDefinition.get(K.data, List.class);

            ChangeTrigger trigger = new ChangeTrigger(type, new HashSet<>(filterable));
            this.setChangeTrigger(trigger);
        } else {
            if (triggerDefinition != null) {
                throw new IllegalModelException("Trigger definition cannot be provided for PUSH model in "
                        + definition.getSource());
            }
        }
    }

    private void parseIntegegrationModel(TypedValueMapAccessor definition) {
        String model = definition.getSimpleValue(K.integrationModel);
        ModelSourceIntegrationModel integrationModel = ModelSourceIntegrationModel.from(model);
        if (integrationModel == null) {
            throw new IllegalModelException("Unknown @integrationModel in: " + definition.getSource());
        }
        this.setIntegrationModel(integrationModel);
    }

}
