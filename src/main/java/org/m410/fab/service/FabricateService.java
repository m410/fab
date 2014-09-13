package org.m410.fab.service;

import org.m410.fab.builder.*;
import org.m410.fab.config.ConfigProvider;

import java.util.Map;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface FabricateService {

    FabricateService addCommand(Command c);

    void addCommandModifier(CommandModifier c);

    void execute(String[] taskList) throws Exception;

    void addConfigProvider(ConfigProvider provider);

    void addConfig(Map<String, Object> config);

    void addCommandListener(CommandListener c);

    void addTaskListener(TaskListener t);

    void addConfigListener(ConfigListener c);

    void postStartupWiring();
}
