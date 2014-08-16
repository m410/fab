package org.m410.fab.service;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Command;
import org.m410.fab.builder.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author m410
 */
public class CommandListTask implements Task{
    private List<Command> commands = new ArrayList<>();

    public void add(Command command) {
        commands.add(command);
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public String getDescription() {
        return "List all commands";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        commands.stream().forEach(c->{
            context.cli().println(c.getName() + " - "+ c.getDescription());
        });
    }
}
