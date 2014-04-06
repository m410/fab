package org.m410.fab.config;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author m410
 */
@RunWith(JUnit4.class)
public class ConfigBuilderTest {

    private String env = "dev";
    private Map<String, Object> projectFile;
    private List<ConfigProvider> configProviders = new ArrayList<>();

    @Before @SuppressWarnings("unchecked")
    public void setup() {
        InputStream configFileInput = getClass().getResourceAsStream("/garden.test.fab.yml");
        projectFile = (Map<String, Object>) new Yaml().load(configFileInput);
        configProviders.add(new ConfigProvider() {

            @Override
            public Map<String, Object> config() {
                HashMap<String,Object> map = new HashMap<>();
                final HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("package_docs", true);
                map.put("build", hashMap);
                return map;
            }

            @Override
            public Set<String> validate(Map<String, Object> fullConfig) {
                return null;
            }
        });
    }

    @Test
    public void testAll() {
        ConfigContext config = new ConfigBuilder(projectFile)
                .parseLocalProject()
                .applyUnder(configProviders.stream().map(ConfigProvider::config).collect(Collectors.toList()))
                .applyEnvOver(env)
                .build();

        assertNotNull(config);
        assertNotNull(config.getApplication());
        assertNotNull(config.getBuild());
        assertNotNull(config.getDependencies());
        assertEquals(2, config.getDependencies().size());
        assertNotNull(config.getModules());
        assertEquals(3, config.getModules().size());
    }

    @Test
    public void testMakeApplication() {
        HashMap<String,Object> map = new HashMap<>();
        Application application = new ApplicationImpl(map);
        assertNotNull(application);
        assertNotNull(application.getName());
        assertNotNull(application.getOrg());
        assertNotNull(application.getVersion());
        assertNotNull(application.getProperties());
        assertNotNull(application.getApplicationClass());
        assertNotNull(application.getAuthors());
        assertNotNull(application.getDescription());
    }

    @Test
    public void testMakeBuild() {
        HashMap<String,Object> map = new HashMap<>();
        Build build = new BuildImpl(map);

        assertNotNull(build.getCompilerArgs());
        assertNotNull(build.getDefaultCommand());
        assertNotNull(build.getDefaultEnvironment());
        assertNotNull(build.getDefaultLogLevel());
        assertNotNull(build.getDeployTo());
        assertNotNull(build.getLang());
        assertNotNull(build.getLangVersion());
        assertNotNull(build.getPackageClassifier());
        assertNotNull(build.getPackageName());
        assertNotNull(build.getPackageName());
        assertNotNull(build.getPublishTo());
        assertNotNull(build.getResourceDir());
        assertNotNull(build.getSourceDir());
        assertNotNull(build.getSourceOutputDir());
        assertNotNull(build.getTargetDir());
        assertNotNull(build.getTestDir());
        assertNotNull(build.getTestOutputDir());
        assertNotNull(build.getTestResourceDir());
        assertNotNull(build.getVcs());
        assertNotNull(build.getWebappDir());

        assertTrue(false);
    }

    @Test
    public void testMakeDependencies() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("name","name1");
        map.put("scope","name2");
        map.put("org","name3");
        map.put("rev","name4");
        map.put("transitive",false);
        Dependency dependency = new Dependency(map);
        assertNotNull(dependency.getName());
        assertNotNull(dependency.getOrg());
        assertNotNull(dependency.getRev());
        assertNotNull(dependency.getScope());
        assertNotNull(dependency.isTransitive());
    }

    @Test
    public void testMakeModules() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("name","name1");
        map.put("scope","name2");
        map.put("org","name3");
        map.put("version","name4");
        map.put("random",false);
        Module module = new ModuleImpl(map);
        assertNotNull(module.getName());
        assertNotNull(module.getOrg());
        assertNotNull(module.getProperties());
//        assertNotNull(module.getType());
        assertNotNull(module.getVersion());
        assertEquals(2, module.getProperties().size());
    }

    @Test
    public void testApplyUnder() {
        ConfigContext config = new ConfigBuilder(projectFile)
                .parseLocalProject()
                .applyUnder(configProviders.stream().map(ConfigProvider::config).collect(Collectors.toList()))
                .applyEnvOver(env)
                .build();

        assertNotNull(config);
        assertNotNull(config.getBuild());
        assertNotNull(config.getBuild().isPackageDocs());
        assertTrue(config.getBuild().isPackageDocs());
    }

    @Test
    public void testApplyOver() {
        ConfigContext config = new ConfigBuilder(projectFile)
                .parseLocalProject()
                .applyUnder(configProviders.stream().map(ConfigProvider::config).collect(Collectors.toList()))
                .applyEnvOver(env)
                .build();

        assertNotNull(config);
        assertNotNull(config.getApplication());
        assertNotNull(config.getApplication().getOrg());
        assertEquals("org.m410.development",config.getApplication().getOrg());
    }
}
