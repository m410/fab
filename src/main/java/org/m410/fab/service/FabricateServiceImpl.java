package org.m410.fab.service;

import org.m410.fab.builder.*;
import org.m410.fab.config.ConfigBuilder;
import org.m410.fab.config.ConfigContext;
import org.m410.fab.config.ConfigProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.stream.Collectors;

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
    private List<ConfigProvider> configProviders = new ArrayList<>();

    @Override
    public FabricateService addCommand(Command c) {
        commandListeners.stream().forEach(it -> it.notify(new CommandEvent(c)));
        commands.add(c);
        return this;
    }

    @Override
    public void addConfigProvider(ConfigProvider provider) {
        configProviders.add(provider);
    }

    @Override
    public void addFullConfig(Map<String, Object> fullConfig) {
        configProviders.add(new ConfigProviderImpl(fullConfig));
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
        commandModifiers.stream().forEach(commandModifier -> {
            commands.stream().forEach(command -> {
                commandModifier.modify(command);
                commandListeners.stream().forEach(l -> l.notify(new CommandEvent(command)));
            });
        });
    }

    @Override
    public void execute(String[] args) throws Exception {

        // todo need to get defaults from base config
        String env = extractEnvironment(args);
        String logLevel = extractLogLevel(args);

        BuildContext buildContext = configureInitialBuildContext(env, logLevel);

        // check each task to see if it takes args
        // only the last command can take args
        for (String arg : Arrays.asList(args)) {
            for (Command command : commands) {
                if (command.getName().equals(arg)) {
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
        ConfigBuilder builder = new ConfigBuilder(configProviders.get(0).config()).parseLocalProject();
        configProviders.stream().skip(1).map(ConfigProvider::config).forEach(builder::applyOver);
        ConfigContext config = builder.applyEnvOver(env).build();

        return new BuildContextImpl(
                new CliStdOutImpl(logLevel),
                config.getApplication(),
                config.getBuild(),
                env,
                config.getDependencies(),
                config.getModules());
    }


    public File projectFile(File projectRoot) {
        File[] files = projectRoot.listFiles((dir, filename) -> filename.endsWith(".fab.yml"));

        if (files == null)
            throw new NoConfigurationFileException();

        if (files.length > 1)
            throw new ToManyConfigurationFilesException();

        if (files.length == 0)
            throw new NoConfigurationFileException();

        return files[0];
    }
}
