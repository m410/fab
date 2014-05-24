package org.m410.fab.service;

import org.m410.fab.builder.Command;
import org.m410.fab.builder.Step;
import org.m410.fab.service.internal.*;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author Michael Fortin
 */
public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {

        ServiceReference fabricateServiceReference = context.getServiceReference(FabricateService.class.getName());

        FabricateService fabricateService = (FabricateService) context.getService(fabricateServiceReference);
        fabricateService.addCommand(
                        new Command("deploy", "Copies artifacts to a destination", false)
                                .withStep(new Step("default").append(new DeployTask()))
                ).addCommand(
                        new Command("publish", "Copies artifacts to a remote maven repository", false)
                                .withStep(new Step("default").append(new RemotePublishTask()))
                ).addCommand(
                        new Command("publish-local", "Copies artifacts to a local maven repository", false)
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
                ).addCommand(
                        new Command("help", "Display Help", false)
                                .withStep(new Step("default").append(new HelpTask()))
                ).addCommand(
                        new Command("bundles", "Display build bundles", false)
                                .withStep(new Step("default").append(new BundleTask()))
                );
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }
}
