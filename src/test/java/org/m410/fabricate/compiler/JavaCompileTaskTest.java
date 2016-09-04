package org.m410.fabricate.compiler;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.m410.config.YamlConfig;
import org.m410.fabricate.builder.BuildContextImpl;
import org.m410.fabricate.builder.Cli;
import org.m410.fabricate.config.Build;
import org.m410.fabricate.config.BuildImpl;
import org.m410.fabricate.config.Dependency;
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

import static org.junit.Assert.*;

/**
 * @author m410
 */
public class JavaCompileTaskTest {

    Cli cli = new Cli() {
        Logger log = LoggerFactory.getLogger(this.getClass());
        @Override public String ask(String question) { log.debug(question); return ""; }
        @Override public void warn(String in) { log.warn(in); }
        @Override public void info(String in) { log.info(in); }
        @Override public void debug(String in) { log.debug(in); }
        @Override public void error(String in) { log.error(in); }
        @Override public void println(String s) { System.out.println(s); }
    };

    @Test
    public void testCompile() throws ConfigurationException {
        JavaCompileTask task = new JavaCompileTask(JavaCompileTask.COMPILE_SRC);
        Build build = new BuildImpl(YamlConfig.load(new File("src/test/resources/test.yml")));
        List<Dependency> deps = new ArrayList<>();
        BuildContextImpl context = new BuildContextImpl(cli,null,build,"dev",deps,null);

        try {
            task.execute(context);
            assertTrue("compiled",true);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail("Failed to compile");
        }
    }
}
