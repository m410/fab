package org.m410.fab.garden.support;


import javax.servlet.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public final class ProxyFilter implements Filter, SourceMonitor.Event {

    private final Class<ServletRequest> ReqCls = ServletRequest.class;
    private final Class<ServletResponse> resCls = ServletResponse.class;
    private final Class<FilterConfig> configCls = FilterConfig.class;
    private final Class<FilterChain> filterChainCls = FilterChain.class;

    private FilterConfig filterConfig;
    private Class filterClass;
    private Object filterInstance;
    private ClassLoader classLoader;
    private String filterClassName;

    private SourceMonitor sourceMonitor;


    public ProxyFilter(SourceMonitor sourceMonitor) {
        this.sourceMonitor = sourceMonitor;
        sourceMonitor.addChangeListener(this);
    }

    @Override
    public void changed() {
        this.filterInstance = null;
    }

    public void setDelegateName(String filterClassName) {
        this.filterClassName = filterClassName;
    }

    public void init(FilterConfig config) {
        filterConfig = config;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        ClassLoader loader = (ClassLoader)req.getServletContext().getAttribute("classLoader");
        Thread.currentThread().setContextClassLoader(loader);

        if (filterInstance == null || classLoader == null || loader != classLoader) {
            classLoader = loader;

            try {
                filterClass = classLoader.loadClass(filterClassName);
                filterInstance = filterClass.newInstance();
                filterClass.getMethod("init", configCls).invoke(filterInstance, filterConfig);
            }
            catch (ClassNotFoundException|InstantiationException|
                    IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                throw new ClassLoaderRuntimeException(e);
            }
        }

        try {
            filterClass.getMethod("doFilter", ReqCls, resCls, filterChainCls)
                    .invoke(filterInstance, req, res, chain);
        }
        catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
            throw new ClassLoaderRuntimeException(e);
        }
    }

    public void destroy() {}
}
