package org.m410.fabricate.builder;

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
