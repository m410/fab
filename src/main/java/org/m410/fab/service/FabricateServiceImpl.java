package org.m410.fab.service;

import org.m410.fab.builder.*;
import org.m410.fab.config.ConfigBuilder;
import org.m410.fab.config.ConfigContext;
import org.m410.fab.config.ConfigProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private List<ConfigProvider> configProviders = new ArrayList<>();

    @Override
    public FabricateService addCommand(Command c) {
//        commandListeners.stream().forEach(it -> it.notify(new CommandEvent()));
        for (CommandListener commandListener : commandListeners) {
            commandListener.notify(new CommandEvent(c));
        }
        commands.add(c);
        return this;
    }

    @Override
    public void addConfigProvider(ConfigProvider provider) {
        configProviders.add(provider);
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
    public void execute(String[] args) throws Exception {
        String env = extractEnvironment(args);
        String logLevel = extractLogLevel(args);
        BuildContext buildContext = configureInitialBuildContext(env, logLevel);

        // list commands
        // list tasks

        // list dependencies
        // list build bundles

        // check each task to see if it takes args
        // only the last command can take args
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
    }

    private String extractLogLevel(String[] args) {
        return "debug";
    }

    private String extractEnvironment(String[] args) {
        return "dev";
    }

    @SuppressWarnings("unchecked")
    BuildContext configureInitialBuildContext(String env, String logLevel) throws FileNotFoundException {
        FileInputStream configFileInput = new FileInputStream(projectFile(new File("")));
        ConfigContext config = new ConfigBuilder(((Map<String,Object>)new Yaml().load(configFileInput)))
                .parseLocalProject()
                .applyUnder(configProviders.stream().map(ConfigProvider::config).collect(Collectors.toList()))
                .applyEnvOver(env)
                .build();

        return new BuildContextImpl(
                new CliStdOutImpl(logLevel),
                config.getApplication(),
                config.getBuild(),
                env,
                config.getDependencies(),
                config.getModules());
    }

    public File projectFile(File projectRoot){
        File[] files = projectRoot.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".fab.yml");
            }
        });

        if(files.length >1)
            throw new ToManyConfigurationFilesException();

        if(files.length == 0)
            throw new NoConfigurationFileException();

        return files[0];
    }
}
