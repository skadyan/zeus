package com.cumulativeminds.zeus.core.meta;

import com.cumulativeminds.zeus.core.CoreException;

public class IllegalModelException extends CoreException {
    private static final long serialVersionUID = 1L;

    public IllegalModelException(String errorCode, Object... args) {
        super(errorCode, args);
    }
}
