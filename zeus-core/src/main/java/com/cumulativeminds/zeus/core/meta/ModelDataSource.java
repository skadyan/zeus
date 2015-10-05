package com.cumulativeminds.zeus.core.meta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.cumulativeminds.zeus.core.spi.ItemReader;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;
import com.cumulativeminds.zeus.intergration.ChangeTrigger;
import com.cumulativeminds.zeus.intergration.ChangeTrigger.ArgType;
import com.cumulativeminds.zeus.intergration.ChangeTriggerType;
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

    protected void configure(Model owner, ModelBuilder modelBuilder) {
        TypedValueMapAccessor definition = getDefinition();

        parseIntegegrationModel(definition);

        parseChangeTrigger(definition, modelBuilder);
    }

    @PostConstruct
    protected void parse() {

    }

    private void parseChangeTrigger(TypedValueMapAccessor definition, ModelBuilder modelBuilder) {
        TypedValueMapAccessor triggerDefinition = definition.getNestedObject(K.changeTrigger);

        if (triggerDefinition == null) {
            throw new IllegalModelException("Trigger definition must be provided model in "
                    + definition.getSource());
        }
        ChangeTriggerType type = toChangeTriggerType(triggerDefinition.getSimpleValue(K.type));
        if (type == null) {
            throw new IllegalModelException("Trigger type must be provided model in "
                    + definition.getSource() + " It can be one of " + Arrays.toString(ChangeTriggerType.values()));
        }

        if (getIntegrationModel() == ModelSourceIntegrationModel.PULL) {
            if (type != ChangeTriggerType.urlencoded) {
                throw new IllegalModelException("Trigger type must be urlencoded for PULL integration mode in "
                        + definition.getSource());
            }
        }

        List<TypedValueMapAccessor> list = triggerDefinition.getList(K.data);
        Map<String, ChangeTrigger.ArgType> args = parseArgs(list, modelBuilder);
        ChangeTrigger trigger = new ChangeTrigger(type, args);

        this.setChangeTrigger(trigger);

    }

    private Map<String, ArgType> parseArgs(List<TypedValueMapAccessor> list, ModelBuilder modelBuilder) {
        Map<String, ArgType> args = new LinkedHashMap<>();
        for (TypedValueMapAccessor typedValueMapAccessor : list) {
            String name = typedValueMapAccessor.ensureAndGetSingleKey();
            TypedValueMapAccessor def = typedValueMapAccessor.getNestedObject(name);
            String type = def.getSimpleValue(K.type);
            String format = def.getSimpleValue(K.format);
            boolean required = def.getBooleanValue(K.required);
            ArgType arg = new ArgType(modelBuilder.toJavaType(type, format), format, required);
            args.put(name, arg);
        }

        return args;
    }

    private ChangeTriggerType toChangeTriggerType(String text) {
        return ChangeTriggerType.fromText(text);
    }

    private void parseIntegegrationModel(TypedValueMapAccessor definition) {
        String model = definition.getSimpleValue(K.integrationModel);
        ModelSourceIntegrationModel integrationModel = ModelSourceIntegrationModel.from(model);
        if (integrationModel == null) {
            throw new IllegalModelException("Unknown @integrationModel in: " + definition.getSource());
        }
        this.setIntegrationModel(integrationModel);
    }

    public Class<? extends ItemReader> getChangeItemReader() {
        return null;
    }
}
