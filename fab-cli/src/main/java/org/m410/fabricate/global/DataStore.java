package org.m410.fabricate.global;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.m410.config.YamlConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Michael Fortin
 */
public class DataStore {
    final List<YamlConfiguration> configurations = new ArrayList<>();

    public DataStore(String home) throws IOException, ConfigurationException {
        final Path archetypes = FileSystems.getDefault().getPath(home, "archetypes");
        final File[] files = archetypes.toFile().listFiles();

        if (files == null) {
            System.err.println("Could not fine archetype db directory");
            System.exit(1);
        }

        Arrays.stream(files).forEach(file -> {
            YamlConfiguration configuration = new YamlConfiguration();
            try (FileReader in = new FileReader(file)) {
                configuration.read(in);
                configurations.add(configuration);
            }
            catch (ConfigurationException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<YamlConfiguration> findByName(String s) {
        return configurations.stream()
                .filter(ihc -> s.equals(ihc.getString("name")))
                .findAny();
    }

    public List<YamlConfiguration> searchBy(String s) {
        return configurations.stream()
                .filter(ihc -> ihc.getString("name").contains(s) || ihc.getString("org").contains(s))
                .collect(Collectors.toList());
    }
}
