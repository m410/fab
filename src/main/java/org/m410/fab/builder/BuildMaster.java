package org.m410.fab.builder;

import java.util.List;
import java.util.Optional;

/**
 * @author m410
 */
public interface BuildMaster {

    List<Command> getCommands();

    Optional<Command> getCommandWithName(String name);

    void addCommand(Command command);
}
