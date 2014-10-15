package org.m410.fabricate.garden;

import org.m410.fabricate.builder.Command;
import org.m410.fabricate.builder.Step;
import org.m410.fabricate.service.FabricateService;
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
                        .filter(m->m.getName().equals("generate-resources"))
                        .findFirst()
                        .ifPresent(m->{
                            m.append(new MakeConfigTask());
                            m.append(new WebXmlTask());
                        });

            }
        });
    }

    public void stop(BundleContext context) throws Exception {
    }

}
