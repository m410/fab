package org.m410.fab.garden.support;

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
public final class ProxyServlet extends HttpServlet implements SourceMonitor.Event {

    private final Class<HttpServletRequest> reqCls = HttpServletRequest.class;
    private final Class<HttpServletResponse> resCls = HttpServletResponse.class;
    private final Class<ServletConfig> configCls = ServletConfig.class;

    private ServletConfig servletConfig;
    private Class servletClass;
    private Object servletInstance;
    private ClassLoader classLoader;

    private String servletClassName;

    private SourceMonitor sourceMonitor;

    public ProxyServlet(SourceMonitor sourceMonitor) {
        this.sourceMonitor = sourceMonitor;
        sourceMonitor.addChangeListener(this);
    }

    @Override
    public void changed() {
        this.servletInstance = null;
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
        ClassLoader loader = (ClassLoader)req.getServletContext().getAttribute("classLoader");
        Thread.currentThread().setContextClassLoader(loader);

        if (servletInstance == null || classLoader == null || loader != classLoader) {
            classLoader = loader;

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

        if(sourceMonitor.getStatus()== SourceMonitor.Status.Ok) {
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
