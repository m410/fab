package org.m410.fabricate.compiler;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.m410.config.YamlConfig;
import org.m410.fabricate.builder.BuildContextImpl;
import org.m410.fabricate.builder.Cli;
import org.m410.fabricate.config.ProjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author m410
 */
public class JavaCompileTaskTest {

    Cli cli = new Cli() {
        Logger log = LoggerFactory.getLogger(this.getClass());
        @Override public String ask(String question) { log.debug(question); return ""; }
        @Override public void warn(String in) { log.warn(in); }
        @Override public void info(String in) { log.info(in); }

        @Override
        public void debug(String in) {
            System.out.println(in);
        }
        @Override public void error(String in) { log.error(in); }
        @Override public void println(String s) { System.out.println(s); }
    };

    @Test
    public void testCompile() throws ConfigurationException {
        JavaCompileTask task = new JavaCompileTask(JavaCompileTask.COMPILE_SRC);
        ProjectContext ctx = new ProjectContext(YamlConfig.load(new File("src/test/resources/test.yml")));
        BuildContextImpl context = new BuildContextImpl(cli, ctx, "env", null);

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
