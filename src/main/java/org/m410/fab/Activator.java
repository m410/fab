package org.m410.fab;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.m410.fab.service.HelloService;
import org.m410.fab.service.HelloServiceImpl;

/**
 * And to read this service:
 *
 * <pre>
 * public class Activator implements BundleActivator {
 * ServiceReference helloServiceReference;
 * public void start(BundleContext context) throws Exception {
 * System.out.println("Hello World!!");
 * helloServiceReference= context.getServiceReference(HelloService.class.getName());
 * HelloService helloService =(HelloService)context.getService(helloServiceReference);
 * System.out.println(helloService.sayHello());
 * }
 * public void stop(BundleContext context) throws Exception {
 * System.out.println("Goodbye World!!");
 * context.ungetService(helloServiceReference);
 * }
 * }
 * </pre>
 *
 */
public class Activator implements BundleActivator {
    ServiceRegistration helloServiceRegistration;

    public void start(BundleContext context) throws Exception {
        System.out.println("start with context:" + context);
        HelloService helloService = new HelloServiceImpl();
        final String name = HelloService.class.getName();
        helloServiceRegistration = context.registerService(name, helloService, null);
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("stop with context:" + context);
        helloServiceRegistration.unregister();
    }

}
