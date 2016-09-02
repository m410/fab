package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A reference to an OSGI bundle that is loaded at build run.  Typically only in the base
 * references to modules or archetypes.
 *
 * @author m410
 */
public final class BundleRef extends ReferenceBase {
    private final String symbolicName;

    public BundleRef(HierarchicalConfiguration<ImmutableNode> c, Type type, Level l, String env) {
        this.configuration = (BaseHierarchicalConfiguration) c;
        this.name = c.getString("name");
        this.org = c.getString("organization");
        this.version = c.getString("version");
        this.environment = env;
        this.type = type; // archetype or module
        this.level = l; // always remote
        this.symbolicName = c.getString("symbolicName");

        try {
            this.remoteReference = c.containsKey("remote_reference") ?
                                   new URL(c.getString("remote_reference")) :
                                   toUrl();
        }
        catch (MalformedURLException e) {
            throw new InvalidConfigurationException("invalid url: " + c.getString("remote_reference"), e);
        }
    }

    private URL toUrl() throws MalformedURLException {
        // todo implement me
        return null;
    }

    public String getSymbolicName() {
        if (symbolicName != null) {
            return symbolicName;
        }
        else {
            return name;
        }
    }

    @Override
    public String toString() {
        return "BundleRef(" +
               "name='" + name + '\'' +
               ", org='" + org + '\'' +
               ", version='" + version + '\'' +
               ", url=" + remoteReference +
               ')';
    }

}
