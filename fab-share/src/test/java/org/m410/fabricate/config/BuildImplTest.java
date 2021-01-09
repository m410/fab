package org.m410.fabricate.config;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Michael Fortin
 */
public class BuildImplTest {

    @Test
    public void testMakeBuild() throws ConfigurationException {
        Build build = new BuildImpl(Fixtures.config());

        assertNotNull(build.getCompilerArgs());
        assertNotNull(build.getDefaultCommand());
        assertNotNull(build.getDefaultEnvironment());
        assertNotNull(build.getDefaultLogLevel());
        assertNotNull(build.getLang());
        assertNotNull(build.getLangVersion());
        assertNotNull(build.getResourceDir());
        assertNotNull(build.getSourceDir());
        assertNotNull(build.getSourceOutputDir());
        assertNotNull(build.getTargetDir());
        assertNotNull(build.getTestDir());
        assertNotNull(build.getTestOutputDir());
        assertNotNull(build.getTestResourceDir());
        assertNotNull(build.getVcs());
        assertNotNull(build.getWebappDir());

//        assertNotNull(build.getPackageClassifier());
//        assertNotNull(build.getPackageName());
    }
}