package org.m410.fabricate.service;

import org.m410.fabricate.builder.*;
import org.m410.fabricate.config.ConfigBuilder;
import org.m410.fabricate.config.ConfigContext;
import org.m410.fabricate.config.ConfigProvider;
import org.m410.fabricate.service.internal.serialize.CachedProject;
import org.m410.fabricate.service.internal.serialize.HashUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.FileSystems;
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

    /**
     * Adds all the configuration files to the context.
     * @param config
     */
    @Override
    public void addConfig(Map<String, Object> config) {
        configProviders.add(new ConfigProviderImpl(config));
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

        dumpContext(buildContext, env);
    }

    private String extractLogLevel(String[] args) {
        return "info";
    }

    private String extractEnvironment(String[] args) {
        return "dev";
    }

    @SuppressWarnings("unchecked")
    BuildContext configureInitialBuildContext(String env, String logLevel) throws Exception {
        ConfigBuilder builder = new ConfigBuilder(configProviders.get(0).config()).parseLocalProject();
        configProviders.stream().skip(1).map(ConfigProvider::config).forEach(builder::applyOver);
        ConfigContext config = builder.applyEnvOver(env).build();

        String hash = HashUtil.getMD5Checksum(HashUtil.projectFile());
        File projectCacheFile = projectConfigFile(config.getBuild().getCacheDir(), env);

        if(!projectCacheFile.exists()) {
            return new BuildContextImpl(new CliStdOutImpl(logLevel), config, hash, env);
        }
        else {
            CachedProject cachedProject = (CachedProject) new Yaml(new Constructor(CachedProject.class))
                    .load(new FileInputStream(projectCacheFile));

            if(!hash.equals(cachedProject.getHash())) {
                projectCacheFile.delete();
                return new BuildContextImpl(new CliStdOutImpl(logLevel), config, hash, env);
            }
            else {
                return new BuildContextImpl(new CliStdOutImpl(logLevel), cachedProject, env);
            }
        }
    }

    private File projectConfigFile(String cacheDir, String env) {
        final String fileName = "project." + env + ".yml";
        return FileSystems.getDefault().getPath(cacheDir, fileName).toFile();
    }

    public void dumpContext(BuildContext ctx, String env) throws IOException {
        CachedProject cachedProject = new CachedProject(ctx);
        File projectCacheFile = projectConfigFile(ctx.getBuild().getCacheDir(), env);
        final FileWriter fileWriter = new FileWriter(projectCacheFile);
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        options.setAllowReadOnlyProperties(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        new Yaml(options).dump(cachedProject, fileWriter);
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
