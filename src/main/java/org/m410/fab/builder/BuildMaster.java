package org.m410.fab.builder;

import java.util.List;

/**
 * @author m410
 */
public interface BuildMaster {

    List<Command> getCommands();

    Command getCommandWithName(String name);

    void addCommand(Command command);
}
