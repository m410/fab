package org.m410.fab.garden.jetty;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public final class ProxyServlet extends HttpServlet implements ReloadingEventListener {

    private final Class<HttpServletRequest> reqCls = HttpServletRequest.class;
    private final Class<HttpServletResponse> resCls = HttpServletResponse.class;
    private final Class<ServletConfig> configCls = ServletConfig.class;

    private ServletConfig servletConfig;
    private Class servletClass;
    private Object servletInstance;
    private ClassLoader classLoader;

    private String servletClassName;
    private Object application;
    private SourceMonitor sourceMonitor;

    public ProxyServlet(SourceMonitor sourceMonitor) {
        this.sourceMonitor = sourceMonitor;
        sourceMonitor.addReloadingListener(this);
    }

    @Override
    public void onChange(ReloadingEvent reloadingEvent) {
        if(reloadingEvent.isRelease()) {
            this.servletInstance = null;
        }
        else {
            this.classLoader = reloadingEvent.getClassLoader();
            this.application = reloadingEvent.getApplication();
        }
    }

    public void setServletClass(Class servletClass) {
        this.servletClass = servletClass;
    }

    public void setDelegateName(String name) {
        this.servletClassName = name;
    }

    @Override
    public void init(ServletConfig config) {
        servletConfig = config;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        Thread.currentThread().setContextClassLoader(classLoader);

        if(sourceMonitor.getStatus()== SourceMonitor.Status.Ok) {
            if (servletInstance == null ) {
                req.getServletContext().setAttribute("application",application);

                try {
                    servletClass = classLoader.loadClass(servletClassName);
                    servletInstance = servletClass.newInstance();
                    servletClass.getMethod("init", configCls).invoke(servletInstance, servletConfig);
                }
                catch (ClassNotFoundException| InstantiationException| IllegalAccessException|
                        InvocationTargetException| NoSuchMethodException e) {
                    throw new ClassLoaderRuntimeException(e);
                }
            }

            try {
                servletClass.getMethod("service", reqCls, resCls).invoke(servletInstance, req, res);
            }
            catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                throw new ClassLoaderRuntimeException(e);
            }
        }
        else {
            servletInstance = null;
            sourceMonitor.renderStatusPage(req,res);
        }
    }
}
