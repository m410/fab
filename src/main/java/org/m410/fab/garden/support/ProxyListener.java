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
public class ProxyListener implements EventListener {
    String className;

    public void setClassName(String className) {
        this.className = className;
    }

}