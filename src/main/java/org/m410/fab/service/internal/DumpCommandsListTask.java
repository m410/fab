package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Command;
import org.m410.fab.builder.Task;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author m410
 */
public class DumpCommandsListTask implements Task {

    private SortedMap<String,String> commands = new TreeMap<String,String>();

    @Override
    public String getName() {
        return "list-commands";
    }

    @Override
    public String getDescription() {
        return "Dump the list of commands to standard out";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        commands.entrySet().stream().forEach(entrySet ->{
            context.cli().println("  " + entrySet.getKey() +" - "+ entrySet.getValue());
        });
    }

    public void addTask(Command command) {
        commands.put(command.getName(),command.getDescription());
    }
}
