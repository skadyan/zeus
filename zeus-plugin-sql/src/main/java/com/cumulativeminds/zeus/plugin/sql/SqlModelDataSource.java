package com.cumulativeminds.zeus.plugin.sql;

import static org.springframework.util.StringUtils.hasText;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.util.StringUtils;

import com.cumulativeminds.zeus.core.meta.IllegalModelException;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;
import com.cumulativeminds.zeus.intergration.ChangeTrigger;
import com.cumulativeminds.zeus.intergration.ModelSourceIntegrationModel;

public class SqlModelDataSource extends ModelDataSource {
    public static final String TYPE = "RDBMS";
    private String dataSource;
    private String sourceViewName;
    private String sourceViewNameAlias;
    private String changeViewName;
    private String changeViewNameAlias;

    public SqlModelDataSource(TypedValueMapAccessor definition) {
        super(TYPE, definition);
    }

    @PostConstruct
    public void parse() {
        TypedValueMapAccessor definition = getDefinition();
        String source = definition.getSource();

        parseDataSource(definition, source);
        parseIntegegrationModel(definition, source);
        parseChangeTrigger(definition, source);

        sourceViewName = definition.getSimpleValue(SqlK.sourceViewName);
        sourceViewNameAlias = definition.getSimpleValue(SqlK.sourceViewNameAlias);
        changeViewName = definition.getSimpleValue(SqlK.changeViewName);
        changeViewNameAlias = definition.getSimpleValue(SqlK.changeViewNameAlias);
    }

    private void parseChangeTrigger(TypedValueMapAccessor definition, String source) {
        TypedValueMapAccessor triggerDefinition = definition.getNestedObject(SqlK.changeTrigger);
        if (getIntegrationModel() == ModelSourceIntegrationModel.PULL) {
            if (triggerDefinition == null) {
                throw new IllegalModelException("Trigger definition must be provided for PULL model in " + source);
            }
            String type = triggerDefinition.getSimpleValue(SqlK.type);
            @SuppressWarnings("unchecked")
            List<String> filterable = triggerDefinition.get(SqlK.data, List.class);

            ChangeTrigger trigger = new ChangeTrigger(type, filterable);
            this.setChangeTrigger(trigger);
        } else {
            if (triggerDefinition != null) {
                throw new IllegalModelException("Trigger definition cannot be provided for PUSH model in " + source);
            }
        }
    }

    private void parseIntegegrationModel(TypedValueMapAccessor definition, String source) {
        String model = definition.getSimpleValue(SqlK.integrationModel);
        ModelSourceIntegrationModel integrationModel = ModelSourceIntegrationModel.from(model);
        if (integrationModel == null) {
            throw new IllegalModelException("Unknown @integrationModel in: " + source);
        }
        this.setIntegrationModel(integrationModel);
    }

    private void parseDataSource(TypedValueMapAccessor definition, String source) {
        String dataSource = definition.getSimpleValue(SqlK.dataSource);
        if (!StringUtils.hasText(dataSource)) {
            throw new IllegalMonitorStateException("Please specify @dataSource in: " + source);
        }
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(Model owner) {
        super.configure(owner);
        String code = owner.getCode().toUpperCase();
        if (!hasText(sourceViewName)) {
            changeViewName = "T_ZEUS_" + code;
        }
        if (!hasText(sourceViewNameAlias)) {
            changeViewName = "e";
        }

        if (!hasText(changeViewName)) {
            changeViewName = "T_ZEUS_" + code + "_CHG";
        }

        if (!hasText(changeViewNameAlias)) {
            changeViewNameAlias = "c";
        }
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getChangeViewName() {
        return changeViewName;
    }

    public String getChangeViewNameAlias() {
        return changeViewNameAlias;
    }

    public String getSourceViewName() {
        return sourceViewName;
    }

    public String getSourceViewNameAlias() {
        return sourceViewNameAlias;
    }
}