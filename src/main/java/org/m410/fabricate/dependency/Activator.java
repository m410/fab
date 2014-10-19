package org.m410.fabricate.dependency;

import org.m410.fabricate.service.FabricateService;
import org.osgi.framework.*;

/**
 * @author Michael Fortin
 */
public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());
        FabricateService fabricateService = (FabricateService) context.getService(fabricateServiceReference);

        fabricateService.addCommandModifier(modifier -> {
                    modifier.getSteps().stream()
                            .filter(step -> step.getName().equalsIgnoreCase("resolve-compile-dependencies"))
                            .forEach(step -> step.append(new IvyDependencyTask()));

                    if(modifier.getName().equalsIgnoreCase("dependencies")) {
                        modifier.getSteps().stream()
                                .filter(m->m.getName().equals("initialize"))
                                .findFirst()
                                .ifPresent(m->m.append(new IvyDependencyTask()));
                    }
                }


        );
    }

    public void stop(BundleContext context) throws Exception {
    }

}
