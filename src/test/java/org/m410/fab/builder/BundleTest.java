package org.m410.fab.builder;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author m410
 */
@RunWith(JUnit4.class)
public class BundleTest {

    @Test
    public void buildContext() {
        BuildContext ctx = new BuildContextImpl(null,null,null,null,null,null);
        assertNotNull(ctx);
    }
}
