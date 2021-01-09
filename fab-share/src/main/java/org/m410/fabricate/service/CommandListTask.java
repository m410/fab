package org.m410.fabricate.service;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Command;
import org.m410.fabricate.builder.Task;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author m410
 */
public class CommandListTask implements Task{
    private SortedSet<Command> commands = new TreeSet<>();

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
        commands.forEach(c -> {
            context.cli().println(c.getName() + " - "+ c.getDescription());
        });
    }
}
