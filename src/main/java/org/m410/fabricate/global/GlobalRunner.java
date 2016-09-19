package org.m410.fabricate.global;

import java.util.Arrays;
import java.util.List;

/**
 * Fabricate commands that can be run from outside a project directory.
 *
 * @author m410
 */
public final class GlobalRunner {
    private static final List<Cmd> cmds = Arrays.asList(
            new Cmd("create", "Create a new project by name", (dataStore, args) -> {
                new CreateCmd(dataStore, args).run();
            }),
            new Cmd("search", "Search for modules or archetypes by name or organization", (dataStore, args) -> {
                new SearchCmd(dataStore, args).run();
            }),
            new Cmd("info", "Display project or module information by name", (dataStore, args) -> {
                new InfoCmd(dataStore, args).run();
            }));

    private final List<String> args;
    private DataStore dataStore = new DataStore();

    public GlobalRunner(List<String> args) {
        this.args = args;
        // todo need to parse or load the project/module data store
    }

    public void run() throws Exception {
        cmds.stream().filter(c -> c.getName().equals(args.get(0))).findFirst().ifPresent(cmd -> {
            cmd.getCmd().accept(dataStore, args.toArray(new String[args.size()]));
        });
    }

    public static boolean isGlobalCommand(String s) {
        return cmds.stream().filter(c -> c.getName().equals(s)).findFirst().isPresent();
    }

    public void outputCommands() {
        cmds.forEach(cmd -> {
            System.out.print(cmd.getName());
            System.out.print(" - ");
            System.out.println(cmd.getDescription());
        });
    }

    interface CmdRunner {
        void accept(DataStore store, String[] args);
    }

    private static final class Cmd {
        private final String name;
        private final String description;
        private final CmdRunner cmd;

        public Cmd(String name, String description, CmdRunner cmd) {
            this.name = name;
            this.description = description;
            this.cmd = cmd;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public CmdRunner getCmd() {
            return cmd;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Cmd cmd = (Cmd) o;

            return name.equals(cmd.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
