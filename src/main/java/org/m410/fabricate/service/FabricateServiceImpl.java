package org.m410.fabricate.service;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.m410.fabricate.builder.*;
import org.m410.fabricate.config.ProjectContext;
import org.m410.fabricate.service.internal.serialize.HashUtil;

import java.util.*;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public class FabricateServiceImpl implements FabricateService {
    private List<Command> commands = new ArrayList<>();
    private List<CommandModifier> commandModifiers = new ArrayList<>();
    private List<CommandListener> commandListeners = new ArrayList<>();
    private List<TaskListener> taskListeners = new ArrayList<>();
    private List<ConfigListener> configListeners = new ArrayList<>();

    private ImmutableHierarchicalConfiguration configuration;
    private String environment = "default";
    private String logLevel = "info";

    @Override
    public FabricateService addCommand(Command c) {
        commandListeners.forEach(it -> it.notify(new CommandEvent(c)));
        commands.add(c);
        return this;
    }

    /**
     * Adds all the configuration files to the context.
     * @param config
     */
    @Override
    public void addConfig(ImmutableHierarchicalConfiguration config) {
        configuration = config;
    }

    @Override
    public void addCommandModifier(CommandModifier c) {
        commandModifiers.add(c);
    }

    @Override
    public void addCommandListener(CommandListener c) {
        commandListeners.add(c);
    }

    @Override
    public void addTaskListener(TaskListener t) {
        taskListeners.add(t);
    }

    @Override
    public void addConfigListener(ConfigListener c) {
        configListeners.add(c);
    }

    @Override
    public void postStartupWiring() {
        commandModifiers.forEach(commandModifier -> {
            commands.forEach(command -> {
                commandModifier.modify(command);
                commandListeners.forEach(l -> l.notify(new CommandEvent(command)));
            });
        });
    }

    @Override
    public void execute(String[] args) throws Exception {

        BuildContext buildContext = configureInitialBuildContext(environment, logLevel);

        // todo check each task to see if it takes args
        for (String arg : Arrays.asList(args)) {
            Optional<Command> cmd = commands.stream().filter(c -> c.getName().equals(arg)).findFirst();

            if(cmd.isPresent()) {
                Command command = cmd.get();

                for (CommandListener commandListener : commandListeners)
                    commandListener.notify(new CommandEvent(command));

                command.execute(taskListeners, buildContext);
            }
            else {
                System.out.println("ERROR: Unknown command '"+arg+"'");
            }
        }
    }

    @Override
    public void setEnv(String env) {
        this.environment = env;
    }

    // todo replace with commons yaml configuration
    @SuppressWarnings("unchecked")
    BuildContext configureInitialBuildContext(String env, String logLevel) throws Exception {
        ProjectContext config = new ProjectContext(configuration);

        // todo really isn't the services responsibility to cache and read the file.
        // should be done by the cli.
        String hash = HashUtil.getMD5Checksum(HashUtil.projectFile());
        return new BuildContextImpl(new CliStdOutImpl(logLevel), config, hash, env);
    }

}
