package com.cumulativeminds.zeus.exceptions;

import com.cumulativeminds.zeus.core.CoreException;

public class BadRequestException extends javax.ws.rs.BadRequestException {
    private static final long serialVersionUID = -6870012057391911239L;

    public BadRequestException(String code, Object... args) {
        super(CoreException.resolveCodeToMessage(code, args));
    }

}
