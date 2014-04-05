package org.m410.fab.service;

/**
 * @author m410
 */
public class NoConfigurationFileException extends RuntimeException {
    public NoConfigurationFileException() {
    }

    public NoConfigurationFileException(String message) {
        super(message);
    }

    public NoConfigurationFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoConfigurationFileException(Throwable cause) {
        super(cause);
    }

    public NoConfigurationFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
