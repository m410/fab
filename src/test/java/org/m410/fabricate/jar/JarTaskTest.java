package org.m410.fabricate.jar;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.m410.config.YamlConfig;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.BuildContextImpl;
import org.m410.fabricate.builder.Cli;
import org.m410.fabricate.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author m410
 */
public class JarTaskTest {

    BuildContext context;

    @Before
    public void before() throws ConfigurationException {
        List<Dependency> deps = new ArrayList<Dependency>();
        deps.add(new Dependency("compile","org.apache.commons","commons-lang3","3.3.2",false));

        final BaseHierarchicalConfiguration load = YamlConfig.load(new File("src/test/resources/test.yml"));

        Build build = new BuildImpl(load);
        Application app = new ApplicationImpl(load);

        Cli cli = new Cli() {
            Logger log = LoggerFactory.getLogger(this.getClass());
            @Override public String ask(String question) { log.debug(question); return ""; }
            @Override public void warn(String in) { log.warn(in); }
            @Override public void info(String in) { log.info(in); }
            @Override public void debug(String in) { log.debug(in); }
            @Override public void error(String in) { log.error(in); }
            @Override public void println(String s) { System.out.println(s); }
        };

        context = new BuildContextImpl(cli,app,build,"dev",deps,null);
    }

    @Test
    public void testMakeJar() throws Exception {
        JarTask task = new JarTask();
        task.execute(context);
        final String jar = "src/test/assets/target/test-app-1.0.0.jar";
        File f = FileSystems.getDefault().getPath(jar).toFile();
        assert(f.exists());
    }
}
