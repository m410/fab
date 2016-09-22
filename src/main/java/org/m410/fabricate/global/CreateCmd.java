package org.m410.fabricate.global;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.Optional;

/**
 * @author Michael Fortin
 */
public class CreateCmd implements Runnable {
    String name[];
    DataStore dataStore;

    public CreateCmd(DataStore dataStore, String[] name) {
        this.name = name;
        this.dataStore = dataStore;
    }

    @Override
    public void run() {
        if (name.length == 2) {
            final Optional<ImmutableHierarchicalConfiguration> byName = dataStore.findByName(name[1]);
            // todo init project
            System.out.println("config: " + byName);
        }
    }
}
