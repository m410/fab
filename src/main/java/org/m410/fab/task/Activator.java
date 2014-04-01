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
 * <p/>
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


    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());

        FabricateService fabricateService = (FabricateService) context.getService(fabricateServiceReference);
        fabricateService.addCommand(
                new Command("build", "Build project", false)
                        .withStep(new Step("pre-compile"))
                        .withStep(new Step("compile").withTask(new BuildTask()))
                        .withStep(new Step("post-compile"))
                        .withStep(new Step("pre-test-compile"))
                        .withStep(new Step("test-compile"))
                        .withStep(new Step("post-test-compile"))
                        .withStep(new Step("pre-tet"))
                        .withStep(new Step("test"))
                        .withStep(new Step("post-test"))
                        .withStep(new Step("pre-package"))
                        .withStep(new Step("package"))
                        .withStep(new Step("post-package"))
        );
    }

    public void stop(BundleContext context) throws Exception {
    }

}
