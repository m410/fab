package org.m410.fab.garden.support;

import java.util.EventListener;

/**
 * ServletContextListener
 * ServletContextAttributeListener
 * HttpSessionListener
 * HttpSessionAttributeListener
 *
 * @author Michael Fortin
 */
// todo need several types of listeners, not just the eventListener
// todo need proxy factory
public final class ProxyListener implements EventListener, SourceMonitor.Event {
    String className;
    private SourceMonitor sourceMonitor;

    public ProxyListener(SourceMonitor sourceMonitor) {
        this.sourceMonitor = sourceMonitor;
        sourceMonitor.addChangeListener(this);
    }

    @Override
    public void changed() {

    }

    public void setDelegateName(String className) {
        this.className = className;
    }

}