package com.cumulativeminds.zeus.api.controller.inbound;

import static com.cumulativeminds.zeus.api.controller.SimpleStatus.status;
import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cumulativeminds.zeus.api.internal.JerseyUtils;
import com.cumulativeminds.zeus.api.internal.ModelParam;
import com.cumulativeminds.zeus.core.meta.Model;

@Component
@Path("/change")
@Consumes({ MediaType.MULTIPART_FORM_DATA })
public class MultipartFormDataChangeController extends InboundController {

    @Value("${serverDataDirectory}")
    private File directory;

    public MultipartFormDataChangeController() {
    }

    @POST
    public Response acceptChange(FormDataMultiPart body, @ModelParam Model model,
            @Context UriInfo uriInfo,
            @Context HttpHeaders httpHeaders) throws IOException {
        log.debug("{} - change request of multipart/form-data received", model);
        MultivaluedMap<String, Object> parameters = JerseyUtils.getQueryParametes(uriInfo);
        for (BodyPart bodyPart : body.getBodyParts()) {
            ContentDisposition contentDisposition = bodyPart.getContentDisposition();
            String name = (String) contentDisposition.getParameters().get("name");

            String fileName = contentDisposition.getFileName();
            if (StringUtils.hasText(fileName)) {
                File file = receice(bodyPart.getEntityAs(InputStream.class), contentDisposition);
                parameters.add(name, file);
            } else {
                parameters.add(name, bodyPart.getEntityAs(String.class));
            }
        }
        log.debug("Parameters: {}. File received successfully.", parameters);

        return status("File uploaded sucessfully").build();
    }

    private File receice(InputStream fileInputStream, ContentDisposition fileMetaData) {
        try {
            File file = new File(directory, fileMetaData.getFileName());
            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream out = new FileOutputStream(file);
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            closeQuietly(fileInputStream);
            closeQuietly(out);

            return file;
        } catch (IOException e) {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }

    }
}
