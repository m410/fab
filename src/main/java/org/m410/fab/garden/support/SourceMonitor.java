package org.m410.fab.garden.support;

import java.io.File;

/**
 * @author <a href="mailto:mifortin@deloitte.com">Michael Fortin</a>
 */
public final class SourceMonitor {
    File sourceBaseDir;

    public SourceMonitor(File sourceBaseDir) {

    }

    public Status getStatus() {
        return Status.Ok;
    }

    public enum Status {
        Ok,
        Modified,
        Compiling,
        Failed
    }
}
