package org.m410.fab.task;

import org.m410.fab.service.FabricateService;
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
 */
public class Activator implements BundleActivator {

    
    public void start(BundleContext context) throws Exception {
        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());

        FabricateService fabricateService =(FabricateService)context.getService(fabricateServiceReference);
        fabricateService.addConfiguration("task configuration 1");
        fabricateService.addCommand("task command 1");
        fabricateService.addTask("task 1");
    }

    public void stop(BundleContext context) throws Exception {
    }
}
