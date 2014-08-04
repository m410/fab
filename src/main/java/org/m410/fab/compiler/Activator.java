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

        fabricateService.addCommandModifier(modifier -> {
            if(modifier.getName().equalsIgnoreCase("build"))
                modifier.lastStep().append(new CompileTask());
        });

//        fabricateService.addCommandListener((e) -> System.out.println(" -> command event: " + e));
//        fabricateService.addTaskListener((e) -> System.out.println(" -> task event:" + e));
    }

    public void stop(BundleContext context) throws Exception {
    }

}
