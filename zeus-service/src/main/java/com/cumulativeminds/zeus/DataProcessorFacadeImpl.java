package com.cumulativeminds.zeus;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.compute.DataProcessorFacade;
import com.cumulativeminds.zeus.core.spi.Change;

@Component
public class DataProcessorFacadeImpl implements DataProcessorFacade {
    private ModelService modelService;

    @Inject
    public DataProcessorFacadeImpl(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public void submit(Change change) {
        modelService.submit(change);
    }
}
