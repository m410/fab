package org.m410.fab.garden;

import org.m410.fab.builder.Command;
import org.m410.fab.builder.Step;
import org.m410.fab.garden.jetty.Jetty9Task;
import org.m410.fab.garden.orm.OrmXmlTask;
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
                        .filter(m->m.getName().equals("generate-resources"))
                        .findFirst()
                        .ifPresent(m->{
                            m.append(new MakeConfigTask());
                            m.append(new LogbackXmlTask());
                            m.append(new PersistenceXmlTask());
                            m.append(new WebXmlTask());
                        });
                command.getSteps().stream()
                        .filter(m->m.getName().equals("process-classes"))
                        .findFirst()
                        .ifPresent(m->m.append(new OrmXmlTask()));
            }
        });

        fabricateService.addCommand(new Command("jetty9","Run jetty",false)
                .withStep(new Step("default").append(new Jetty9Task())));
    }

    public void stop(BundleContext context) throws Exception {
    }

}
