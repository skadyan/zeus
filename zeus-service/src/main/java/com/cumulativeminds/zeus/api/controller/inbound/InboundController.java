package com.cumulativeminds.zeus.api.controller.inbound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.springframework.core.convert.ConversionService;

import com.cumulativeminds.zeus.api.controller.ModelAwareController;
import com.cumulativeminds.zeus.compute.DataProcessorFacade;
import com.cumulativeminds.zeus.intergration.ChangeTrigger.ArgType;

public abstract class InboundController extends ModelAwareController {

    protected final ConversionService conversionService;
    protected final DataProcessorFacade dataProcessorFacade;

    public InboundController(ConversionService conversionService, DataProcessorFacade dataProcessorFacade) {
        this.conversionService = conversionService;
        this.dataProcessorFacade = dataProcessorFacade;
    }

    protected Map<String, Object> mapParametersToObject(MultivaluedMap<String, String> parameters, Map<String, ArgType> arguments) {
        Map<String, Object> typedValue = new HashMap<String, Object>();
        parameters.forEach((k, v) -> {
            ArgType type = arguments.get(k);
            if (type == null) {
                typedValue.put(k, v);
            } else {
                if (v.size() > 1) {
                    ArrayList<Object> typed = new ArrayList<>();
                    v.forEach(e -> typed.add(conversionService.convert(e, type.getType())));
                    typedValue.put(k, typed);
                } else {
                    typedValue.put(k, conversionService.convert(v.get(0), type.getType()));
                }
            }
        });
        return typedValue;
    }

}
