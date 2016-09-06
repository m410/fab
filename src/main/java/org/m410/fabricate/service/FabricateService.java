package org.m410.fabricate.service;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.m410.fabricate.builder.*;
import org.m410.fabricate.config.ConfigProvider;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface FabricateService {

    FabricateService addCommand(Command c);

    void addCommandModifier(CommandModifier c);

    void execute(String[] taskList) throws Exception;

    void setEnv(String env);

    void addConfig(BaseHierarchicalConfiguration config);

    void addCommandListener(CommandListener c);

    void addTaskListener(TaskListener t);

    void addConfigListener(ConfigListener c);

    void postStartupWiring();
}
