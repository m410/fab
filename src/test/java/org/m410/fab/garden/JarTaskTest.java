package org.m410.fab.garden;

import org.junit.Before;
import org.junit.Test;
import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.BuildContextImpl;
import org.m410.fab.builder.Cli;
import org.m410.fab.config.*;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author m410
 */
public class JarTaskTest {

    BuildContext context;

    @Before
    public void before() {
        Map<String,Object> map = new HashMap<>();

        final String src = "src/test/assets/src";
        map.put("sourceOutputDir", FileSystems.getDefault().getPath(src).toFile().getAbsolutePath());
        final String tgt = "src/test/assets/target";
        map.put("targetDir", FileSystems.getDefault().getPath(tgt).toFile().getAbsolutePath());

        map.put("name", "test-app");
        map.put("org","org.m410.test");
        map.put("description","none");
        map.put("version","1.0.0");
        map.put("applicationClass","none");
        map.put("authors","none");
        map.put("properties", new HashMap<String,Object>());

        List<Dependency> deps = new ArrayList<Dependency>();
        deps.add(new Dependency("compile","org.apache.commons","commons-lang3","3.3.2",false));

        Build build = new BuildImpl(map);
        Application app = new ApplicationImpl(map);

        Cli cli = new Cli() {
            @Override public String ask(String question) { System.out.println(question); return ""; }
            @Override public void warn(String in) { System.out.println(in); }
            @Override public void info(String in) { System.out.println(in); }
            @Override public void debug(String in) { System.out.println(in); }
            @Override public void error(String in) { System.out.println(in); }
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
