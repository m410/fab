package org.m410.fabricate.jar;

import org.m410.fab.service.FabricateService;
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
                            .filter(step -> step.getName().equalsIgnoreCase("package"))
                            .forEach(step -> step.append(new JarTask()));
                }
        );
    }

    public void stop(BundleContext context) throws Exception {
        fabricateServiceRegistration.unregister();
    }

}
