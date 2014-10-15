package org.m410.fabricate.compiler;

import org.m410.fabricate.service.FabricateService;
import org.osgi.framework.*;

/**
 * @author Michael Fortin
 */
public class Activator implements BundleActivator {
    ServiceRegistration fabricateServiceRegistration;

    public void start(BundleContext context) throws Exception {
        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());
        FabricateService fabricateService = (FabricateService) context.getService(fabricateServiceReference);

        fabricateService.addCommandModifier(modifier -> {
                    modifier.getSteps().stream()
                            .filter(step -> step.getName().equalsIgnoreCase("compile"))
                            .forEach(step -> step.append(new JavaCompileTask(false)));

                    modifier.getSteps().stream()
                            .filter(step -> step.getName().equalsIgnoreCase("test-compile"))
                            .forEach(step -> step.append(new JavaCompileTask(true)));
                }
        );
    }

    public void stop(BundleContext context) throws Exception {
        fabricateServiceRegistration.unregister();
    }

}
