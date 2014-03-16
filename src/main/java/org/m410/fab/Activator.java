package org.m410.fab;

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

    public void start(BundleContext context) throws Exception {
        System.out.println("start with context:" + context);
        FabricateService fabricateService = new FabricateServiceImpl();
        final String name = FabricateService.class.getName();
        fabricateServiceRegistration = context.registerService(name, fabricateService, null);

        // open file, read modules
        final String s = "file://Users/m410/Projects/fab(ricate)/fab-java-task-bundle" +
                "/target/fab-java-task-bundle-0.1-SNAPSHOT.jar";
        context.installBundle(s);

        // load modules by url

        fabricateService.addConfiguration("Some Config");
        // need to know what command to run
        fabricateService.execute(new String[]{});
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("stop with context:" + context);
        fabricateServiceRegistration.unregister();
    }

}
