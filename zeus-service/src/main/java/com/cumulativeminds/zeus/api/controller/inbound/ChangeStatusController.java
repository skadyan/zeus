package com.cumulativeminds.zeus.api.controller.inbound;

import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.api.controller.SimpleStatus;
import com.cumulativeminds.zeus.api.internal.ModelParam;
import com.cumulativeminds.zeus.compute.DataProcessorFacade;
import com.cumulativeminds.zeus.core.meta.Model;

@Component
@Path("change/status/{changeId}")
public class ChangeStatusController extends InboundController {

    @Inject
    public ChangeStatusController(ConversionService conversionService, DataProcessorFacade dataProcessorFacade) {
        super(conversionService, dataProcessorFacade);
    }

    @GET
    public Response status(@ModelParam Model model,
            @PathParam("changeId") String changeId) {
        return SimpleStatus.status("Status of change:" + changeId + " is not known").build();
    }
}
