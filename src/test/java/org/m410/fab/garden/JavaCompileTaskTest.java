package org.m410.fab.garden;

import org.junit.Test;
import org.m410.fab.builder.BuildContextImpl;
import org.m410.fab.config.Build;
import org.m410.fab.config.BuildImpl;
import org.m410.fab.config.Dependency;

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
        final String src = "src/test/compiler-test-resources/src";
        map.put("sourceDir", FileSystems.getDefault().getPath(src).toFile().getAbsolutePath());
        final String tgt = "src/test/compiler-test-resources/target";
        map.put("sourceOutputDir", FileSystems.getDefault().getPath(tgt).toFile().getAbsolutePath());
        Build build = new BuildImpl(map);
        List<Dependency> deps = new ArrayList<>();

        BuildContextImpl context = new BuildContextImpl(null,null,build,"dev",deps,null);

        try {
            task.execute(context);
            assertTrue("compiled",true);
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue("Failed to compile", false);
        }

    }
}
