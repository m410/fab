package org.m410.fab.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author m410
 */
@RunWith(JUnit4.class)
public class FabricateServiceImplTest {

    @Test
    public void buildContext() {
        FabricateService s = new FabricateServiceImpl();
        assertNotNull(s);
    }
}
