package org.m410.fab.service;

/**
 * @author m410
 */
public class ToManyConfigurationFilesException extends RuntimeException {
    public ToManyConfigurationFilesException() {
    }

    public ToManyConfigurationFilesException(String message) {
        super(message);
    }

    public ToManyConfigurationFilesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToManyConfigurationFilesException(Throwable cause) {
        super(cause);
    }

    public ToManyConfigurationFilesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
