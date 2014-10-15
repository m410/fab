package org.m410.fabricate.global;

import org.m410.fabricate.config.ProjectConfig;

/**
 * @author m410
 */
public class ProjectCommands {
    public void create() {
        System.out.println("## create project");
    }

    public void resources(ProjectConfig build) {
        System.out.println("");
        System.out.println("  " + build.getArchetype());
        build.getConfigurations().stream().forEach(r -> System.out.println("  " + r));
//        System.out.println("");
//        build.resources().stream().forEach(r -> System.out.println("  " + r));
        System.out.println("");
    }
}
