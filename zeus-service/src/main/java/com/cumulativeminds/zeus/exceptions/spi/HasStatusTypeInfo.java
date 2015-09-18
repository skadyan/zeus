package com.cumulativeminds.zeus.exceptions.spi;

import javax.ws.rs.core.Response.StatusType;

public interface HasStatusTypeInfo {
    StatusType getStatusType();
}
