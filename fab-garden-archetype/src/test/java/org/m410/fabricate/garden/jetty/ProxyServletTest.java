package org.m410.fabricate.garden.jetty;

import org.junit.Before;
import org.junit.Test;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.BuildContextImpl;
import org.m410.fabricate.builder.Cli;
import org.m410.fabricate.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * @author m410
 */
public class ProxyServletTest {

    BuildContext context;

    @Before
    public void before() {
        Map<String,Object> map = new HashMap<>();
        final String tgt = "src/test/dependencies";
        final File file = FileSystems.getDefault().getPath(tgt).toFile();

        if(!file.exists() && !file.mkdirs())
            fail("could not make cache directory");

        map.put("cacheDir", file.getAbsolutePath());
        map.put("name", "test-app");
        map.put("org","org.m410.test");
        map.put("description","none");
        map.put("version","1.0.0");
        map.put("applicationClass","none");
        map.put("authors","none");
        map.put("properties", new HashMap<String,Object>());

        List<Dependency> deps = new ArrayList<>();
        deps.add(new Dependency("compile","org.apache.commons","commons-lang3","3.3.2",false));

        Build build = null;//new BuildImpl(map);
        Application app = null;//new ApplicationImpl(map);

        Cli cli = new Cli() {
            Logger log = LoggerFactory.getLogger(this.getClass());
            @Override public String ask(String question) { log.debug(question); return ""; }
            @Override public void warn(String in) { log.warn(in); }
            @Override public void info(String in) { log.info(in); }
            @Override public void debug(String in) { log.debug(in); }
            @Override public void error(String in) { log.error(in); }
            @Override public void println(String s) { System.out.println(s); }
        };

        context = new BuildContextImpl(cli,app,build,"development",deps,null);
    }


    @Test
    public void testStart() throws Exception {
        // load an app in a child classloader, call it from the servlet proxy
    }
}
