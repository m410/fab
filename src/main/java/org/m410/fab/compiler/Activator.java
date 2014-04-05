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

        fabricateService.addCommandListener(new CommandListener() {
            @Override
            public void notify(CommandEvent e) {
                System.out.println("command event: " + e);
            }
        });

        fabricateService.addTaskListener(new TaskListener() {
            @Override
            public void notify(TaskEvent e) {
                System.out.println("task event:" + e);
            }
        });
    }

    public void stop(BundleContext context) throws Exception {
    }

}
