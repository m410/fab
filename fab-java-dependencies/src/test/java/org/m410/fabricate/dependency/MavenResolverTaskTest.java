package org.m410.fabricate.dependency;

import org.junit.Test;

/**
 * @author Michael Fortin
 */
public class MavenResolverTaskTest {
    @Test
    public void canresolve() throws Exception {
        MavenResolverTask task = new MavenResolverTask();
        task.execute(null);
    }
}