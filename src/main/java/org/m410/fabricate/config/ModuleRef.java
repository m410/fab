package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Michael Fortin
 */
public final class ModuleRef extends ReferenceBase {
    private final Pattern pattern = Pattern.compile("^(\\w+)\\(([\\w\\.]+):([a-zA-Z0-9-_]+):(.+)\\)$");
    private final String stereotype;

    public ModuleRef(String moduleName, HierarchicalConfiguration<ImmutableNode> c, Type type, Level l, String env) {
        this.type = type;
        this.environment = env;
        this.configuration = (BaseHierarchicalConfiguration) c;
        this.level = l;

        final Matcher matcher = pattern.matcher(moduleName.replaceAll("\\.\\.", "."));

        if (matcher.find()) {
            this.stereotype = matcher.group(1);
            this.org = matcher.group(2);
            this.name = matcher.group(3);
            this.version = matcher.group(4);
        }
        else {
            throw new InvalidConfigurationException("invalid module name: '" + moduleName + "'");
        }

        try {
            this.remoteReference = c.containsKey("remote_reference") ?
                                   new URL(c.getString("remote_reference")) :
                                   null;
        }
        catch (MalformedURLException e) {
            throw new InvalidConfigurationException("invalid url: " + c.getString("remote_reference"), e);
        }
    }

    public String getStereotype() {
        return stereotype;
    }

}
