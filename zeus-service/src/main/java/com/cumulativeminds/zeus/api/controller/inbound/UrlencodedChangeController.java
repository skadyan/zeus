package com.cumulativeminds.zeus.api.controller.inbound;

import static com.cumulativeminds.zeus.api.controller.SimpleStatus.status;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.api.internal.JerseyUtils;
import com.cumulativeminds.zeus.api.internal.ModelParam;
import com.cumulativeminds.zeus.compute.DataProcessorFacade;
import com.cumulativeminds.zeus.core.meta.Exceptions;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.core.spi.Change;
import com.cumulativeminds.zeus.exceptions.BadRequestException;
import com.cumulativeminds.zeus.intergration.ChangeTrigger.ArgType;
import com.cumulativeminds.zeus.intergration.ModelSourceIntegrationModel;

@Component
@Path("/change")
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
public class UrlencodedChangeController extends InboundController {

    @Inject
    public UrlencodedChangeController(ConversionService conversionService, DataProcessorFacade dataProcessorFacade) {
        super(conversionService, dataProcessorFacade);
    }

    @POST
    public Response acceptChange(Form form,
            @ModelParam Model model,
            @Context UriInfo uriInfo,
            @Context HttpHeaders httpHeaders) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); // get logged in username
        log.debug("{} - change request of application/x-www-form-urlencoded is received. user: {}", model, name);

        ModelDataSource meta = model.getModelDataSource();

        validateThisChangeEventIsSupported(model, meta);

        MultivaluedMap<String, String> parameters = JerseyUtils.mergeParameters(uriInfo, form);
        Map<String, ArgType> arguments = model.getModelDataSource().getChangeTrigger().getArguments();
        Map<String, Object> data = mapParametersToObject(parameters, arguments);

        Change change = new Change(model.getCode(), data);

        dataProcessorFacade.submit(change);
        return status("Request accepted").build(Status.ACCEPTED);
    }

    private void validateThisChangeEventIsSupported(Model model, ModelDataSource meta) {
        ModelSourceIntegrationModel integrationModel = meta.getIntegrationModel();

        if (integrationModel == ModelSourceIntegrationModel.PUSH) {
            throw new BadRequestException(Exceptions.UNSUPPORTED_CHANGE_TYPE, MediaType.APPLICATION_FORM_URLENCODED, model);
        }
    }
}
