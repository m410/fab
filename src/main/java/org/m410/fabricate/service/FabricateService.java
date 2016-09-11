package org.m410.fabricate.service;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.m410.fabricate.builder.*;

import java.io.IOException;

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

    void addConfig(String config) throws IOException, ConfigurationException;

    void addCommandListener(CommandListener c);

    void addTaskListener(TaskListener t);

    void addConfigListener(ConfigListener c);

    void postStartupWiring();
}
