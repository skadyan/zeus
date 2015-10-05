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
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;

import com.cumulativeminds.zeus.exceptions.spi.HasAdvise;
import com.cumulativeminds.zeus.exceptions.spi.HasErrorCode;
import com.cumulativeminds.zeus.exceptions.spi.HasLink;
import com.cumulativeminds.zeus.exceptions.spi.HasStatusTypeInfo;

@Configuration
public class ServiceErrorAttributes extends DefaultErrorAttributes {

    public ServiceErrorAttributes() {
    }

    public Map<String, Object> getErrorAttributes(Throwable ex, RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> defaults = super.getErrorAttributes(requestAttributes, includeStackTrace);

        Map<String, Object> attrs = new TreeMap<>(defaults);

        Throwable throwable = ex == null ? getError(requestAttributes) : ex;

        if (throwable instanceof HasStatusTypeInfo) {
            StatusType statusType = ((HasStatusTypeInfo) throwable).getStatusType();
            addStatus(attrs, throwable, statusType);
        } else if (throwable instanceof WebApplicationException) {
            StatusType statusType = ((WebApplicationException) throwable).getResponse().getStatusInfo();
            addStatus(attrs, throwable, statusType);
        }

        if (throwable instanceof HasLink) {
            attrs.put(link.name(), ((HasLink) throwable).getLink());
        }
        if (throwable instanceof HasErrorCode) {
            attrs.put(code.name(), ((HasErrorCode) throwable).getErrorCode());
        }
        if (throwable instanceof HasAdvise) {
            attrs.put(message.name(), ((HasAdvise) throwable).getAdvise());
        }
        if (includeStackTrace && throwable != null) {
            attrs.remove("trace");
            attrs.put(developerMessage.name(), toDeveloperMessage(throwable));
        }

        attrs.put(timestamp.name(), LocalDateTime.now());

        attrs.remove("error", "None");
        attrs.remove("message", "No message available");
        attrs.remove("message", attrs.get("error"));
        attrs.remove("status", 999);

        if (ex != null) {
            if (!(attrs.containsKey("exception") || attrs.containsKey("message"))) {
                attrs.put("exception", ex.getMessage());
            }
        }

        return attrs;
    }

    private void addStatus(Map<String, Object> attrs, Throwable throwable, StatusType statusType) {
        attrs.put(status.name(), statusType.getStatusCode());
        String msg = throwable.getMessage();
        if (msg == null) {
            msg = statusType.getReasonPhrase();
        }
        attrs.put("error", msg);
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

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        return getErrorAttributes(null, requestAttributes, includeStackTrace);
    }

}
