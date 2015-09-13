package com.cumulativeminds.zeus.exceptions.api;

import javax.ws.rs.core.Response.StatusType;

public interface HasStatusTypeInfo {
    StatusType getStatusType();
}
