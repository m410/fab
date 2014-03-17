package org.m410.fab.service;

import org.m410.fab.builder.*;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface FabricateService {

    void addCommand(Command c);

    void addCommandModifier(CommandModifier c);

    void execute(String[] taskList);

    void addConfiguration(String config);

    void addCommandListener(CommandListener c);

    void addTaskListener(TaskListener t);

    void addConfigListener(ConfigListener c);

    void modifyCommands();
}
