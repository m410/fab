package org.m410.fab.loader;

import org.m410.fab.service.FabricateService;
import org.m410.fab.service.FabricateServiceImpl;
import org.osgi.framework.*;

import java.io.File;

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
        FabricateService fabricateService = new FabricateServiceImpl();
        final String name = FabricateService.class.getName();
        fabricateServiceRegistration = context.registerService(name, fabricateService, null);

        // open file, read modules
        final String s = new File("/Users/m410/Projects/" +
                "fab(ricate)/fab-java-task-bundle/target/fab-java-task-0.1-SNAPSHOT.jar")
                .toURI().toURL().toString();
        context.installBundle(s).start();
        // load modules by url

        FabricateService service = (FabricateService)context.getService(fabricateServiceRegistration.getReference());
        service.addConfiguration("some config");

        service.execute(new String[]{});
    }

    public void stop(BundleContext context) throws Exception {
        fabricateServiceRegistration.unregister();
    }

}
