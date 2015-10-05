package com.cumulativeminds.zeus.compute;

import com.cumulativeminds.zeus.core.CoreException;

public class ModelContractViolationException extends CoreException {
    private static final long serialVersionUID = 1L;

    public ModelContractViolationException(String errorCode, Object... args) {
        super(errorCode, args);
    }
}
