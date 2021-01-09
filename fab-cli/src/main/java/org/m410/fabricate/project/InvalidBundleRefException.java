package org.m410.fabricate.project;

import org.m410.fabricate.config.BundleRef;


/**
 * @author m410
 */
public class InvalidBundleRefException extends RuntimeException {
    public InvalidBundleRefException(Exception e, BundleRef s) {
        super(s.toString(),e);
    }
}
