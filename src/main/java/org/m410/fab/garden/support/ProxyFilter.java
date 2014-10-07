package org.m410.fab.garden.support;


import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public final class ProxyFilter implements Filter, ReloadingEventListener {

    private final Class<ServletRequest> ReqCls = ServletRequest.class;
    private final Class<ServletResponse> resCls = ServletResponse.class;
    private final Class<FilterConfig> configCls = FilterConfig.class;
    private final Class<FilterChain> filterChainCls = FilterChain.class;

    private FilterConfig filterConfig;
    private Class filterClass;
    private Object filterInstance;
    private ClassLoader classLoader;
    private String filterClassName;
    private Object application;
    private SourceMonitor sourceMonitor;


    public ProxyFilter(SourceMonitor sourceMonitor) {
        this.sourceMonitor = sourceMonitor;
        sourceMonitor.addReloadingListener(this);
    }

    @Override
    public void onChange(ReloadingEvent reloadingEvent) {
        if(reloadingEvent.isRelease()) {
            this.filterInstance = null;
        }
        else {
            this.classLoader = reloadingEvent.getClassLoader();
            this.application = reloadingEvent.getApplication();
        }
    }

    public void setDelegateName(String filterClassName) {
        this.filterClassName = filterClassName;
    }

    public void init(FilterConfig config) {
        filterConfig = config;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Thread.currentThread().setContextClassLoader(classLoader);

        if (filterInstance == null ) {
            req.getServletContext().setAttribute("application",application);

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

        if(sourceMonitor.getStatus() == SourceMonitor.Status.Ok) {
            try {
                filterClass.getMethod("doFilter", ReqCls, resCls, filterChainCls)
                        .invoke(filterInstance, req, res, chain);
            }
            catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                throw new ClassLoaderRuntimeException(e);
            }
        }
        else {
            filterInstance = null;
            chain.doFilter(req,res);
        }
    }

    public void destroy() {}
}
