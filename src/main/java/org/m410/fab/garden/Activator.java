package org.m410.fab.garden;

import org.m410.fab.builder.Command;
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
        FabricateService fabricateService = (FabricateService) context.getService(fabricateServiceReference);

        fabricateService.addCommandModifier(command -> {
            if(command.getName().equalsIgnoreCase("build")) {
                command.getSteps().stream()
                        .filter(m->m.getName().equals("compile"))
                        .findFirst()
                        .ifPresent(m->m.append(new JavaCompileTask(JavaCompileTask.COMPILE_SRC)));
                command.getSteps().stream()
                        .filter(m -> m.getName().equals("test-compile"))
                        .findFirst()
                        .ifPresent(m -> m.append(new JavaCompileTask(JavaCompileTask.COMPILE_TEST)));
                command.getSteps().stream()
                        .filter(m->m.getName().equals("test"))
                        .findFirst()
                        .ifPresent(m->m.append(new JUnitTestRunnerTask()));
                command.getSteps().stream()
                        .filter(m->m.getName().equals("initialize"))
                        .findFirst()
                        .ifPresent(m->m.append(new IvyDependencyTask()));
                command.getSteps().stream()
                        .filter(m->m.getName().equals("package"))
                        .findFirst()
                        .ifPresent(m->m.append(new JarTask()));
            }

        });

//        fabricateService.addCommand(new Command("tomcat8","Run tomcat",false)
//                .withStep(new Step("default").append(new TomcatReloadingServerTask())));

        fabricateService.addCommand(new Command("jetty9","Run jetty",false)
                .withStep(new Step("default").append(new Jetty9Task())));
    }

    public void stop(BundleContext context) throws Exception {
    }

}
