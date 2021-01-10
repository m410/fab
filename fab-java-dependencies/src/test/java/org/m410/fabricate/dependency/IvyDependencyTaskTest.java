package org.m410.fabricate.dependency;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.m410.config.YamlConfig;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.BuildContextImpl;
import org.m410.fabricate.builder.Cli;
import org.m410.fabricate.config.ProjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.*;


/**
 * @author m410
 */
public class IvyDependencyTaskTest {

    BuildContext context;

    Cli cli = new Cli() {
        Logger log = LoggerFactory.getLogger(this.getClass());
        @Override public String ask(String question) { log.debug(question); return ""; }
        @Override public void warn(String in) { log.warn(in); }
        @Override public void info(String in) { log.info(in); }
        @Override public void debug(String in) { log.debug(in); }
        @Override public void error(String in) { log.error(in); }
        @Override public void println(String s) { System.out.println(s); }
    };


    @Before
    public void before() throws ConfigurationException {
        final BaseHierarchicalConfiguration load = YamlConfig.load(new File("src/test/resources/test.yml"));
        ProjectContext projectContext = new ProjectContext(load);
        context = new BuildContextImpl(cli, projectContext, "hash", "default");
    }

    @Test
    public void testMakeIvyXml() throws Exception {
        IvyDependencyTask task = new IvyDependencyTask();
        File ivyFile = task.makeIvyXml(context);
        assertTrue(ivyFile.exists());
    }

    @Test
    public void testMakeIvySettingXml() throws Exception {
        IvyDependencyTask task = new IvyDependencyTask();
        File ivySettingsFile = task.makeIvySettingsXml(context);
        assertTrue(ivySettingsFile.exists());
    }

    @Test
    public void testDownloadDependency() throws Exception {
        IvyDependencyTask task = new IvyDependencyTask();
        File ivyFile = task.makeIvyXml(context);
        File ivySettingFile = task.makeIvySettingsXml(context);
        task.resolveDependencies(context,ivySettingFile, ivyFile);
        assertNotNull(context.getClasspath());
        assertEquals(4, context.getClasspath().size());
        assertNotNull(context.getClasspath().get("compile"));
        assertNotNull(context.getClasspath().get("test"));
        assertNotNull(context.getClasspath().get("javadoc"));
        assertNotNull(context.getClasspath().get("sources"));
        assertTrue(context.getClasspath().get("compile").length() > 10);
        assertTrue(context.getClasspath().get("test").length() > 10);
//        assertTrue(context.getClasspath().get("javadoc").length() > 10);
//        assertTrue(context.getClasspath().get("sources").length() > 10);
    }
}
