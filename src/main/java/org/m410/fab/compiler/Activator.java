package org.m410.fab.compiler;

import org.m410.fab.builder.Command;
import org.m410.fab.builder.CommandModifier;
import org.m410.fab.builder.Step;
import org.m410.fab.service.FabricateService;
import org.osgi.framework.*;


/**
 * @author Michael Fortin
 */
public class Activator implements BundleActivator {

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());

        FabricateService fabricateService =(FabricateService)context.getService(fabricateServiceReference);

        fabricateService.addCommandModifier(new CommandModifier() {
            @Override public void modify(Command modifier) {
                if(modifier.getName().equalsIgnoreCase("build")) {
                    for (Step step : modifier.getSteps()) {
                        if(step.getName().equalsIgnoreCase("compile"))
                            step.append(new CompileTask());
                    }
                }
            }
        });

    }

    public void stop(BundleContext context) throws Exception {
    }

}
