package org.m410.fabricate.builder;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.junit.Test;
import org.m410.fabricate.config.Fixtures;
import org.m410.fabricate.config.ProjectContext;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Michael Fortin
 */
public class BuildContextImplTest {
    @Test
    public void configAt() throws Exception {
        ImmutableHierarchicalConfiguration config = Fixtures.config();
        BuildContextImpl ctx = new BuildContextImpl(null, new ProjectContext(config), null, "default");

        final Optional<ImmutableHierarchicalConfiguration> c = ctx.configAt("org.m410.mod.persistence", "m410-jpa");
        assertTrue(c.isPresent());

        c.ifPresent(cfg -> {
            assertEquals("org.m410.fab-java-task", cfg.getString("symbolicName"));
        });
    }
}