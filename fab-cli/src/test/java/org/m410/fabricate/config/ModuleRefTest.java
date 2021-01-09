package org.m410.fabricate.config;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * @author Michael Fortin
 */
public class ModuleRefTest {
    @Test
    public void parseName(){
        String x = "persistence(org..m410..mod..persistence:m410-jpa:0..1)";
        final Pattern pattern = Pattern.compile("^(\\w+)\\(([\\w\\.]+):([a-zA-Z0-9-_]+):(.+)\\)$");
        final String input = x.replaceAll("\\.\\.", ".");
        final Matcher matcher = pattern.matcher(input);

        if(matcher.find()) {
            assertEquals("persistence", matcher.group(1));
            assertEquals("org.m410.mod.persistence", matcher.group(2));
            assertEquals("m410-jpa", matcher.group(3));
            assertEquals("0.1", matcher.group(4));
        }
        else {
            fail("pattern not found");
        }
    }

}