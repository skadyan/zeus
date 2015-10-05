package com.cumulativeminds.zeus.api.controller.inbound;

import static com.cumulativeminds.zeus.api.controller.SimpleStatus.status;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.cumulativeminds.zeus.api.internal.ModelParam;
import com.cumulativeminds.zeus.compute.DataProcessorFacade;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.Change;
import com.cumulativeminds.zeus.intergration.ChangeTrigger.ArgType;

@Component
@Path("/change")
public class RawChangeController extends InboundController {

    @Inject
    public RawChangeController(ConversionService conversionService, DataProcessorFacade dataProcessorFacade) {
        super(conversionService, dataProcessorFacade);
    }

    @POST
    public Response acceptChange(InputStream body,
            @ModelParam Model model,
            @Context UriInfo uriInfo,
            @Context HttpHeaders httpHeaders) throws IOException {
        MultivaluedMap<String, String> parameters = uriInfo.getQueryParameters();
        String s = StreamUtils.copyToString(body, Charset.defaultCharset());
        log.debug("Raw Change request received: {} , parameters: {}", s, parameters);

        Map<String, ArgType> arguments = model.getModelDataSource().getChangeTrigger().getArguments();
        Map<String, Object> data = mapParametersToObject(parameters, arguments);
        data.put("raw", s);
        
        Change change = new Change(model.getCode(), data);
        dataProcessorFacade.submit(change);
        return status("Request accepted").build(Status.ACCEPTED);
    }
}
