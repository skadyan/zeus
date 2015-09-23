package com.cumulativeminds.zeus.api.controller.inbound;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.cumulativeminds.zeus.api.controller.SimpleStatus;
import com.cumulativeminds.zeus.api.internal.ModelParam;
import com.cumulativeminds.zeus.core.meta.Model;

@Component
@Path("/change")
public class RawChangeController extends InboundController {

    @POST
    public Response acceptChange(InputStream body,
            @ModelParam Model model,
            @Context UriInfo uriInfo,
            @Context HttpHeaders httpHeaders) throws IOException {
        MultivaluedMap<String, String> parameters = uriInfo.getQueryParameters();
        String s = StreamUtils.copyToString(body, Charset.defaultCharset());
        log.debug("Raw Change request received: {} , parameters: {}", s, parameters);

        return SimpleStatus.status("received").build();
    }
}
