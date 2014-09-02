package org.m410.fab.garden.support;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public class ProxyServletContextEvent extends ServletContextEvent {
    private ServletContextEvent servletContextEvent;
    private ClassLoader classLoader;

    public ProxyServletContextEvent(ServletContextEvent servletContextEvent, ClassLoader classLoader) {
        super(servletContextEvent.getServletContext());
        this.servletContextEvent = servletContextEvent;
        this.classLoader = classLoader;
    }

    @Override
    public ServletContext getServletContext() {
        return new ProxyServletContext(servletContextEvent.getServletContext(), classLoader);
    }
}