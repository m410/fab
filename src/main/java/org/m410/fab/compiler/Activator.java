package org.m410.fab.compiler;

import org.m410.fab.builder.*;
import org.m410.fab.service.FabricateService;
import org.osgi.framework.*;


/**
 * @author Michael Fortin
 */
public class Activator implements BundleActivator {

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());
        FabricateService fabricateService = (FabricateService) context.getService(fabricateServiceReference);

        fabricateService.addCommandModifier(command -> {
            if(command.getName().equalsIgnoreCase("build")) {
                command.getSteps().stream()
                        .filter(m->m.getName().equals("compile"))
                        .findFirst()
                        .ifPresent(m->m.append(new CompileTask()));
                command.getSteps().stream()
                        .filter(m->m.getName().equals("initialize"))
                        .findFirst()
                        .ifPresent(m->m.append(new DependencyTask()));
            }

        });
    }

    public void stop(BundleContext context) throws Exception {
    }

}
