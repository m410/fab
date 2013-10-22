package us.m410.osgi.module;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import us.m410.osgi.builder.BuildService;

/**
 * This is an example service.  As is, you can build and deploy it.  They only thing you will
 * need to change is the buildService.addUnbound(new ExampleTask()); with your own task.
 *
 * @author Michael Fortin
 */
public class ActivatorExample {
    ServiceReference buildServiceReference;

    public void start(BundleContext context) throws Exception {
        buildServiceReference = context.getServiceReference(BuildService.class.getName());
        BuildService buildService =(BuildService)context.getService(buildServiceReference);

        // todo update this to suit your needs.
        buildService.addUnbound(new ExampleTask());
    }

    public void stop(BundleContext context) throws Exception {
        context.ungetService(buildServiceReference);
    }
}
