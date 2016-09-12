package org.m410.fabricate.project;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.junit.Test;
import org.m410.fabricate.config.Project;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author m410
 */
public class ProjectTest {

    @Test
    public void testLoadConfig() throws Exception {
        final String path = "src/test/resources/test-project.fab.yml";
        File file = FileSystems.getDefault().getPath(path).toFile();
        assertTrue("sample config not found",file.exists());

        final File configCacheDir = FileSystems.getDefault().getPath("target/test-output").toFile();

        if(!configCacheDir.exists() && !configCacheDir.mkdirs())
            fail("could not make test output directories");

        Project project = new Project(file,configCacheDir, "default");
        assertNotNull("no config created", project);

        assertNotNull("no archetype", project.getArchetype());
        assertNotNull("no org", project.getArchetype().getOrg());
        assertEquals("org.m410.fabricate.garden", project.getArchetype().getOrg());

        assertNotNull("no version", project.getArchetype().getVersion());
        assertEquals("0.2-SNAPSHOT", project.getArchetype().getVersion());

        assertNotNull("no modules", project.getModuleReferences());
        assertEquals(2, project.getModuleReferences().size());

        assertNotNull("no bundles", project.getBundles());
        assertEquals(3, project.getBundles().size());

        List<HierarchicalConfiguration<ImmutableNode>> dependencies = project.getConfiguration()
                .configurationsAt("dependencies");
        assertNotNull("no dependencies", dependencies);
        //        System.out.println("----");
        //        dependencies.forEach(d ->System.out.println(d.getString("name")));
        //        System.out.println("----");
        assertEquals(30, dependencies.size());

        //
        //        try(StringWriter writer = new StringWriter()) {
        //            ((YamlConfiguration)project.getConfiguration()).write(writer);
        //            System.out.println("----");
        //            System.out.println(writer.toString());
        //            System.out.println("----");
        //        }
    }
}
