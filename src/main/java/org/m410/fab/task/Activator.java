package org.m410.fab.task;

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
        fabricateService.addCommand(
                new Command("build", "Build project", false)
                        .withStep(new Step("pre-build").append(new CounterTask(1)))
                        .withStep(new Step("pre-compile").append(new CounterTask(2)))
                        .withStep(new Step("compile").append(new BuildTask()).append(new CounterTask(3)))
                        .withStep(new Step("post-compile").append(new CounterTask(4)))
                        .withStep(new Step("pre-test-compile").append(new CounterTask(5)))
                        .withStep(new Step("test-compile").append(new CounterTask(6)))
                        .withStep(new Step("post-test-compile").append(new CounterTask(7)))
                        .withStep(new Step("pre-test").append(new CounterTask(8)))
                        .withStep(new Step("test").append(new CounterTask(9)))
                        .withStep(new Step("post-test").append(new CounterTask(10)))
                        .withStep(new Step("pre-package").append(new CounterTask(11)))
                        .withStep(new Step("package").append(new CounterTask(12)))
                        .withStep(new Step("post-package").append(new CounterTask(13)))
                        .withStep(new Step("pre-publish").append(new CounterTask(14)).append(new CounterTask(15)))
                        .withStep(new Step("publish").append(new CounterTask(16)))
                        .withStep(new Step("post-publish").append(new CounterTask(17)))
        ).addCommand(
                new Command("test", "Compile and run unit tests", false)
                        .withStep(new Step("pre-test-compile"))
                        .withStep(new Step("test-compile"))
                        .withStep(new Step("post-test-compile"))
                        .withStep(new Step("pre-test"))
                        .withStep(new Step("test"))
                        .withStep(new Step("post-test"))
        ).addCommand(
                new Command("compile", "Compile sources", false)
                        .withStep(new Step("pre-compile"))
                        .withStep(new Step("compile").append(new BuildTask()))
                        .withStep(new Step("post-compile"))
                        .withStep(new Step("pre-test-compile"))
        ).addCommand(
                new Command("deploy", "Copies artifacts to a destination", false)
                        .withStep(new Step("default").append(new DeployTask()))
        ).addCommand(
                new Command("remote-pub", "Copies artifacts to a remote maven repository", false)
                        .withStep(new Step("default").append(new RemotePublishTask()))
        ).addCommand(
                new Command("local-pub", "Copies artifacts to a local maven repository", false)
                        .withStep(new Step("default").append(new LocalPublishTask()))
        ).addCommand(
                new Command("commands", "List all available commands", false)
                        .withStep(new Step("default").append(new DumpCommandsListTask()))
        ).addCommand(
                new Command("tasks", "List all available tasks", false)
                        .withStep(new Step("default").append(new DumpTaskListTask()))
        ).addCommand(
                new Command("dependencies", "List dependencies for environment", false)
                        .withStep(new Step("default").append(new DependencyDumpTask()))
        ).addCommand(
                new Command("info", "List all environment configuration properties", false)
                        .withStep(new Step("default").append(new InfoDumpTask()))
        );
    }

    public void stop(BundleContext context) throws Exception {
    }

}
