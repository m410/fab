package org.m410.fab.global;

import org.m410.fab.project.ProjectRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author m410
 */
public final class GlobalRunner {
    public static final String cmd0 =  "project-resources";
    public static final String cmd1 =  "project-create";
    public static final String cmd2 =  "archetypes-list";
    public static final String cmd3 =  "archetypes-remove";
    public static final String cmd4 =  "archetypes-add";
    public static final String cmd5 =  "repositories-list";
    public static final String cmd6 =  "repositories-remove";
    public static final String cmd7 =  "repositories-add";


    private static final String[] globalCommands = {cmd0, cmd1, cmd2, cmd3, cmd4, cmd5, cmd6, cmd7};
    private final List<String> args;

    public GlobalRunner(List<String> args) {
        this.args = args;
    }

    public void run() throws Exception {
        switch(args.get(0)) {
            case cmd0:
                final ProjectRunner build = new ProjectRunner(Arrays.asList("build"));
                final File file = build.projectConfigFile(System.getProperty("user.dir"));
                new ProjectCommands().resources(build.loadLocalConfig(file));
                break;
            case cmd1:
                new ProjectCommands().create();
                break;
            case cmd2:
                new ArchetypesCommands().list();
                break;
            case cmd3:
                new ArchetypesCommands().remove();
                break;
            case cmd4:
                new ArchetypesCommands().add();
                break;
            case cmd5:
                new RepositoriesCommands().list();
                break;
            case cmd6:
                new RepositoriesCommands().remove();
                break;
            case cmd7:
                new RepositoriesCommands().add();
                break;
            default:
        }
    }

    public static boolean isGlobalCommand(String s) {
        return Arrays.asList(globalCommands).contains(s);
    }

    public void outputCommands() {
        for (String globalCommand : globalCommands) {
            System.out.println("  " + globalCommand + " - description here");
        }
    }
}
