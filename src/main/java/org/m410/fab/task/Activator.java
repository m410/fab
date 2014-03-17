package org.m410.fab.task;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Command;
import org.m410.fab.builder.Step;
import org.m410.fab.builder.Task;
import org.m410.fab.service.FabricateService;
import org.osgi.framework.*;

import java.util.Arrays;

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
        fabricateService.addConfiguration("configuration1");
        fabricateService.addCommand(
                new Command("build","Build project",false)
                .withStep(new Step("default")
                        .withTask(new BuildTask())
                )
        );
    }

    public void stop(BundleContext context) throws Exception {
    }

}
