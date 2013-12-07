package org.m410.osgi;

import org.osgi.framework.*;
import org.m410.osgi.service.HelloService;

/**
 * And to read this serice:
 * public class Activator implements BundleActivator {
 * ServiceReference helloServiceReference;
 * public void start(BundleContext context) throws Exception {
 * System.out.println("Hello World!!");
 * helloServiceReference= context.getServiceReference(HelloService.class.getName());
 * HelloService helloService =(HelloService)context.getService(helloServiceReference);
 * System.out.println(helloService.sayHello());
 * <p/>
 * }
 * public void stop(BundleContext context) throws Exception {
 * System.out.println("Goodbye World!!");
 * context.ungetService(helloServiceReference);
 * }
 * }
 */
public class Activator implements BundleActivator {

    ServiceReference helloServiceReference;

    public void start(BundleContext context) throws Exception {
        System.out.println("Hello World!!");
        helloServiceReference= context.getServiceReference(HelloService.class.getName());
        HelloService helloService =(HelloService)context.getService(helloServiceReference);
        System.out.println(helloService.sayHello());

    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Goodbye World!!");
        context.ungetService(helloServiceReference);
    }
}
