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

    ServiceReference fabricateServiceReference;

    public void start(BundleContext context) throws Exception {
        System.out.println("Hello World!!");
        fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());
        System.out.println("made service");
        FabricateService helloService =(FabricateService)context.getService(fabricateServiceReference);
        helloService.addConfiguration("task bundle configuration");
        helloService.addCommand("task command 1");
        helloService.addTask("task 1");
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Goodbye task bundle!!");
        context.ungetService(fabricateServiceReference);
    }
}
