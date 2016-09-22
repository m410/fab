package org.m410.fabricate.global;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.m410.config.YamlConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Michael Fortin
 */
public class DataStore {
    final YamlConfiguration configuration;

    public DataStore(String home) throws IOException, ConfigurationException {
        final File dbFile = FileSystems.getDefault().getPath(home, "archetype-db.fab.yml").toFile();
        configuration = new YamlConfiguration();

        try (FileReader in = new FileReader(dbFile)) {
            configuration.read(in);
        }
    }

    public Optional<ImmutableHierarchicalConfiguration> findByName(String s) {
        return configuration.immutableConfigurationsAt("archetypes").stream()
                .filter(ihc -> s.equals(ihc.getString("name")))
                .findAny();
    }

    public List<ImmutableHierarchicalConfiguration> searchBy(String s) {
        return configuration.immutableConfigurationsAt("archetypes").stream()
                .filter(ihc -> ihc.getString("name").contains(s) || ihc.getString("org").contains(s))
                .collect(Collectors.toList());

    }
}
