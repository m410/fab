package org.m410.fab.builder;

/**
 * @author m410
 */
public class CommandEvent {
    private Command command;

    public CommandEvent(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
