package com.cumulativeminds.zeus.core;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;

public class CoreException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final StaticMessageSource NULL = new StaticMessageSource() {
        protected String getMessageInternal(String code, Object[] args, java.util.Locale locale) {
            return String.format(code, args);
        }
    };
    private static MessageSource resourceBundle = NULL;

    private String exceptionCode = Shared.NONE;
    private Object[] args;

    public CoreException(String message) {
        super(message);
    }

    public CoreException(String code, Object... args) {
        super(resolveCodeToMessage(code, args));

        this.exceptionCode = code;
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    private static String resolveCodeToMessage(String code, Object[] args) {
        return resourceBundle.getMessage(code, args, Locale.getDefault());
    }

    public static void setResourceBundle(MessageSource resourceBundle) {
        if (resourceBundle != null) {
            CoreException.resourceBundle = resourceBundle;
        }
    }
}
