package org.m410.fab.loader;

import org.m410.fab.service.FabricateService;
import org.m410.fab.service.FabricateServiceImpl;
import org.osgi.framework.*;

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
    ServiceRegistration fabricateServiceRegistration;

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        FabricateService fabricateService = new FabricateServiceImpl();
        final String name = FabricateService.class.getName();
        fabricateServiceRegistration = context.registerService(name, fabricateService, null);
    }

    public void stop(BundleContext context) throws Exception {
        fabricateServiceRegistration.unregister();
    }

}
