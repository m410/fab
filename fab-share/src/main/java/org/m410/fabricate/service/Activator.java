package org.m410.fabricate.service;

import org.m410.fabricate.builder.Command;
import org.m410.fabricate.builder.Step;
import org.m410.fabricate.service.internal.task.*;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author Michael Fortin
 */
public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {

        context.registerService(FabricateService.class.getName(),new FabricateServiceImpl(),null);

        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());
        FabricateService fabricateService = (FabricateService) context.getService(fabricateServiceReference);

        CommandListTask commandListTask = new CommandListTask();
        fabricateService.addCommandListener(listener ->{
            commandListTask.add(listener.getCommand());
        });


        fabricateService
                .addCommand(
                        new Command("build", "Builds the project and produces it's artifact", false)
                                .withStep(new Step("validate"))
                                .withStep(new Step("initialize"))

                                .withStep(new Step("generate-sources"))
                                .withStep(new Step("process-sources"))
                                .withStep(new Step("generate-resources"))
                                .withStep(new Step("process-resources"))
                                .withStep(new Step("compile"))
                                .withStep(new Step("process-classes"))

                                .withStep(new Step("generate-test-sources"))
                                .withStep(new Step("process-test-sources"))
                                .withStep(new Step("generate-test-resources"))
                                .withStep(new Step("process-test-resources"))
                                .withStep(new Step("test-compile"))
                                .withStep(new Step("process-test-classes"))
                                .withStep(new Step("test"))

                                .withStep(new Step("prepare-package"))
                                .withStep(new Step("package")))
                .addCommand(
                        new Command("build-no-test", "Builds the project and produces it's artifact without testing",
                                false)
                                .withStep(new Step("validate"))
                                .withStep(new Step("initialize"))

                                .withStep(new Step("generate-test-sources"))
                                .withStep(new Step("process-test-sources"))
                                .withStep(new Step("generate-test-resources"))
                                .withStep(new Step("process-test-resources"))
                                .withStep(new Step("test-compile"))
                                .withStep(new Step("process-test-classes"))
                                .withStep(new Step("test"))

                                .withStep(new Step("prepare-package"))
                                .withStep(new Step("package")))
                .addCommand(
                        new Command("compile", "Compiles all source", false)
                                .withStep(new Step("generate-sources"))
                                .withStep(new Step("process-sources"))
                                .withStep(new Step("generate-resources"))
                                .withStep(new Step("process-resources"))
                                .withStep(new Step("compile"))
                                .withStep(new Step("process-classes")))
                .addCommand(
                        new Command("test", "Compiles all test source and runs tests", false)
                                .withStep(new Step("generate-test-sources"))
                                .withStep(new Step("process-test-sources"))
                                .withStep(new Step("generate-test-resources"))
                                .withStep(new Step("process-test-resources"))
                                .withStep(new Step("test-compile"))
                                .withStep(new Step("process-test-classes"))
                                .withStep(new Step("test")))
                .addCommand(
                        new Command("deploy", "Copies artifacts to a destination", false)
                                .withStep(new Step("deploy").append(new DeployTask())))
                .addCommand(
                        new Command("publish", "Copies artifacts to a local maven repository", false)
                                .withStep(new Step("install").append(new LocalPublishTask())))
                .addCommand(
                        new Command("publish-remote", "Copies artifacts remote repository", false)
                                .withStep(new Step("publish").append(new RemotePublishTask())))
                .addCommand(
                        new Command("dependencies", "List dependencies for environment", false)
                                .withStep(new Step("initialize"))
                                .withStep(new Step("run").append(new DependencyDumpTask()))
                )
                .addCommand(
                        new Command("classpaths", "Display each classpath entry", false)
                                .withStep(new Step("initialize"))
                                .withStep(new Step("run").append(new ClasspathDumpTask()))
                           )
                .addCommand(
                        new Command("info", "List all environment configuration properties", false)
                                .withStep(new Step("run").append(new InfoDumpTask())))
                .addCommand(
                        new Command("help", "Display Help", false)
                                .withStep(new Step("run").append(new HelpTask())))
                .addCommand(
                        new Command("clean", "Remove project Artifacts", false)
                                .withStep(new Step("run").append(new CleanTask())))
                .addCommand(
                        new Command("bundles", "Display build bundles", false)
                                .withStep(new Step("run").append(new BundleTask())))
                .addCommand(
                        new Command("tasks", "List all available tasks", false)
                                .withStep(new Step("run").append(commandListTask)));
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }
}
