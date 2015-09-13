package com.cumulativeminds.zeus.exceptions;

import static com.cumulativeminds.zeus.exceptions.ErrorMessageAttributes.code;
import static com.cumulativeminds.zeus.exceptions.ErrorMessageAttributes.developerMessage;
import static com.cumulativeminds.zeus.exceptions.ErrorMessageAttributes.link;
import static com.cumulativeminds.zeus.exceptions.ErrorMessageAttributes.message;
import static com.cumulativeminds.zeus.exceptions.ErrorMessageAttributes.status;
import static com.cumulativeminds.zeus.exceptions.ErrorMessageAttributes.timestamp;

import java.time.LocalDateTime;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.StatusType;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.cumulativeminds.zeus.exceptions.api.HasErrorCode;
import com.cumulativeminds.zeus.exceptions.api.HasLink;
import com.cumulativeminds.zeus.exceptions.api.HasStatusTypeInfo;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class ServiceErrorAttributes extends DefaultErrorAttributes {

    public ServiceErrorAttributes() {
    }

    @Bean
    protected static View error() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // kind of hack; if client does not specify application/json as accepted
        // type.
        jsonView.setContentType("text/html");
        return jsonView;
    }

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> defaults = super.getErrorAttributes(requestAttributes, includeStackTrace);

        Map<String, Object> attrs = new TreeMap<>(defaults);

        attrs.put(timestamp.name(), LocalDateTime.now());

        Throwable throwable = getError(requestAttributes);

        if (throwable instanceof HasStatusTypeInfo) {
            StatusType statusType = ((HasStatusTypeInfo) throwable).getStatusType();
            addStatus(attrs, throwable, statusType);
        } else if (throwable instanceof WebApplicationException) {
            StatusType statusType = ((WebApplicationException) throwable).getResponse().getStatusInfo();
            addStatus(attrs, throwable, statusType);
        } else {
            attrs.put(status.name(), defaults.get("status"));
            attrs.put(message.name(), defaults.get("message"));
        }
        if (throwable instanceof HasLink) {
            attrs.put(link.name(), ((HasLink) throwable).getLink());
        }
        if (throwable instanceof HasErrorCode) {
            attrs.put(code.name(), ((HasErrorCode) throwable).getErrorCode());
        }

        if (includeStackTrace && throwable != null) {
            attrs.remove("trace");
            attrs.put(developerMessage.name(), toDeveloperMessage(throwable));
        }

        return attrs;
    }

    private void addStatus(Map<String, Object> attrs, Throwable throwable, StatusType statusType) {
        attrs.put(status.name(), statusType.getStatusCode());
        String msg = throwable.getMessage();
        if (msg == null) {
            msg = statusType.getReasonPhrase();
        }

        attrs.put(message.name(), msg);
    }

    private String toDeveloperMessage(Throwable exception) {
        StringBuilder sb = new StringBuilder();
        IdentityHashMap<Throwable, Throwable> trackers = new IdentityHashMap<>(6);
        String message = exception.getMessage();
        sb.append(message);
        exception = exception.getCause();
        while (exception != null) {
            if (trackers.containsKey(exception)) {
                break;
            } else {
                sb.append("\ncaused by: ").append(exception.getMessage());
            }
            exception = exception.getCause();
        }

        return sb.toString();
    }

}
