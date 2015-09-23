package com.cumulativeminds.zeus.api.controller.inbound;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.api.controller.ModelAwareController;
import com.cumulativeminds.zeus.api.controller.SimpleStatus;
import com.cumulativeminds.zeus.api.internal.JerseyUtils;
import com.cumulativeminds.zeus.api.internal.ModelParam;
import com.cumulativeminds.zeus.core.meta.Exceptions;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.exceptions.BadRequestException;
import com.cumulativeminds.zeus.intergration.ModelSourceIntegrationModel;

@Component
@Path("/change")
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
public class UrlencodedChangeController extends ModelAwareController {
    public UrlencodedChangeController() {
    }

    @POST
    public Response acceptChange(Form form,
            @ModelParam Model model,
            @Context UriInfo uriInfo,
            @Context HttpHeaders httpHeaders) {
        log.debug("{} - change request of application/x-www-form-urlencoded is received", model);

        ModelDataSource meta = model.getModelDataSource();

        validateThisChangeEventIsSupported(model, meta);

        MultivaluedMap<String, String> parameters = JerseyUtils.mergeParameters(uriInfo, form);
        Set<String> fields = meta.getChangeTrigger().getFields();
        Map<String, Object> data = JerseyUtils.mapParametersToObject(parameters, fields);

        SimpleStatus info = SimpleStatus.status("Change accepted successfully");
        info.with("jobId", "<jobId>");

        return info.build();
    }

    private void validateThisChangeEventIsSupported(Model model, ModelDataSource meta) {
        ModelSourceIntegrationModel integrationModel = meta.getIntegrationModel();

        if (integrationModel == ModelSourceIntegrationModel.PUSH) {
            throw new BadRequestException(Exceptions.UNSUPPORTED_CHANGE_TYPE, MediaType.APPLICATION_FORM_URLENCODED, model);
        }
    }
}
