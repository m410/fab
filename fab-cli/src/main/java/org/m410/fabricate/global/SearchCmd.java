package org.m410.fabricate.global;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.m410.config.YamlConfiguration;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

/**
 * @author Michael Fortin
 */
public class SearchCmd implements Runnable {
    String[] name;
    DataStore dataStore;

    public SearchCmd(DataStore dataStore, String[] name) {
        this.name = name;
        this.dataStore = dataStore;
    }

    @Override
    public void run() {
        if (name.length == 2) {
            List<YamlConfiguration> config = dataStore.searchBy(name[1]);

            config.forEach(c -> {
                final Iterator<String> keys = c.getKeys();

                while (keys.hasNext()) {
                    String next = keys.next();
                    System.out.println("  " + next + ": " + c.getString(next));
                }
                System.out.println("");
            });
        }
        else {
            try (StringWriter writer = new StringWriter()) {
                dataStore.configurations.forEach(c -> {
                    try {
                        c.write(writer);
                        writer.write(System.getProperty("line.separator"));
                    }
                    catch (ConfigurationException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                System.out.println(writer.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

