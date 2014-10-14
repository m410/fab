package org.m410.fabricate.compiler;

import org.junit.Test;
import org.m410.fab.builder.BuildContextImpl;
import org.m410.fab.builder.Cli;
import org.m410.fab.config.Build;
import org.m410.fab.config.BuildImpl;
import org.m410.fab.config.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Test
    public void testCompile() {
        JavaCompileTask task = new JavaCompileTask(JavaCompileTask.COMPILE_SRC);
        Map<String,Object> map = new HashMap<>();
        final String src = "src/test/assets/src";
        map.put("sourceDir", FileSystems.getDefault().getPath(src).toFile().getAbsolutePath());
        final String tgt = "src/test/assets/target/classes";
        map.put("sourceOutputDir", FileSystems.getDefault().getPath(tgt).toFile().getAbsolutePath());
        Build build = new BuildImpl(map);
        List<Dependency> deps = new ArrayList<>();
        Cli cli = new Cli() {
            Logger log = LoggerFactory.getLogger(this.getClass());
            @Override public String ask(String question) { log.debug(question); return ""; }
            @Override public void warn(String in) { log.warn(in); }
            @Override public void info(String in) { log.info(in); }
            @Override public void debug(String in) { log.debug(in); }
            @Override public void error(String in) { log.error(in); }
            @Override public void println(String s) { System.out.println(s); }
        };
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
