package org.m410.fab.service;

import org.m410.fab.builder.*;
import org.m410.fab.config.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public class FabricateServiceImpl implements FabricateService {
    private List<Command> commands = new ArrayList<>();
    private List<CommandModifier> commandModifiers= new ArrayList<>();
    private List<CommandListener> commandListeners = new ArrayList<>();
    private List<TaskListener> taskListeners = new ArrayList<>();
    private List<ConfigListener> configListeners= new ArrayList<>();

    @Override
    public void addCommand(Command c) {
//        commandListeners.stream().forEach(it -> it.notify(new CommandEvent()));
        for (CommandListener commandListener : commandListeners) {
            commandListener.notify(new CommandEvent(c));
        }
        commands.add(c);
    }

    @Override
    public void addConfiguration(String config) {
//        configListeners.stream().forEach(it -> it.notify(new ConfigEvent()));
        for (ConfigListener configListener : configListeners) {
            configListener.notify(new ConfigEvent());
        }
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
    public void modifyCommands() {
        for (CommandModifier commandModifier : commandModifiers) {
            for (Command command : commands) {
                commandModifier.modify(command);
                for (CommandListener commandListener : commandListeners) {
                    commandListener.notify(new CommandEvent(command));
                }
            }
        }
//        commands.stream().forEach(cmd ->{
//            commandModifiers.stream().forEach(mod -> mod.modify(cmd));
//        });
    }

    @Override
    public void execute(String[] args) {
        BuildContext buildContext = configureInitialBuildContext();

        for (String arg : Arrays.asList(args)) {
            for (Command command : commands) {
                if(command.getName().equals(arg)) {
                    for (CommandListener commandListener : commandListeners) {
                        commandListener.notify(new CommandEvent(command));
                    }
                    command.execute(taskListeners, buildContext);
                }
            }
        }
//        Arrays.asList(args).stream().forEach(cmd -> {
//            commands.stream().filter(it -> it.getName().equals(cmd))
//                    .findFirst()
//                    .orElseThrow(UnknownCommandException::new)
//                    .execute(new BuildContextImpl());
//        });
    }

    BuildContext configureInitialBuildContext() {
        // todo setup configuration
        // pull config,
        // apply modules,
        // apply env overrides

        Cli cli = new CliStdOutImpl();
        Application application = new ApplicationImpl();
        String environment = "development";
        Build build = new BuildImpl();
        List<Dependency> dependencies = new ArrayList<>();
        List<Module> modules = new ArrayList<>();
        return new BuildContextImpl(cli, application, build, environment, dependencies, modules);
    }
}
