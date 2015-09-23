package com.cumulativeminds.zeus.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import com.cumulativeminds.zeus.core.CoreException;
import com.cumulativeminds.zeus.exceptions.spi.HasStatusTypeInfo;

public class BadRequestException extends CoreException implements HasStatusTypeInfo {
    private static final long serialVersionUID = -6870012057391911239L;

    public BadRequestException(String code, Object... args) {
        super(code, args);
    }

    @Override
    public StatusType getStatusType() {
        return Status.BAD_REQUEST;
    }

}
