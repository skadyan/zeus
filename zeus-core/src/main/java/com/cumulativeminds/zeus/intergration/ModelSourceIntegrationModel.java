package com.cumulativeminds.zeus.intergration;

import org.springframework.util.StringUtils;

public enum ModelSourceIntegrationModel {
    PUSH, PULL;

    public static ModelSourceIntegrationModel from(String text) {
        if (StringUtils.hasText(text)) {
            ModelSourceIntegrationModel[] values = ModelSourceIntegrationModel.values();
            for (ModelSourceIntegrationModel modelSourceIntegrationModel : values) {
                if (modelSourceIntegrationModel.name().equals(text)) {
                    return modelSourceIntegrationModel;
                }
            }
        }
        return null;
    }
}
