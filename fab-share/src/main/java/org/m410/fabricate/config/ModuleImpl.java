package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author m410
 */
public final class ModuleImpl implements Module {
    private final Pattern pattern = Pattern.compile("^(\\w+)\\(([\\w\\.]+):([a-zA-Z0-9-_]+):(.+)\\)$");

    private Type type;
    private String name;
    private String org;
    private String version;
    private ImmutableConfiguration configuration;

    public ModuleImpl() {
    }

    public ModuleImpl(String name, ImmutableConfiguration configuration) {
        this.configuration = configuration;

        final Matcher matcher = pattern.matcher(name.replaceAll("\\.\\.", "."));

        if (matcher.find()) {
            this.type = Type.of(matcher.group(1));
            this.org = matcher.group(2);
            this.name = matcher.group(3);
            this.version = matcher.group(4);
        }
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOrg() {
        return org;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public ImmutableConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "ModuleImpl{" +
                "version='" + version + '\'' +
                ", org='" + org + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
