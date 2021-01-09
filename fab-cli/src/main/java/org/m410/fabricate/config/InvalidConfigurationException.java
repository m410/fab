package org.m410.fabricate.config;

/**
 * @author m410
 */
public class InvalidConfigurationException extends RuntimeException {
    public InvalidConfigurationException(String s) {
        super(s);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
