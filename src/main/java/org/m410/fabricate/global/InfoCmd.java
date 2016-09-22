package org.m410.fabricate.global;

import org.apache.commons.configuration2.ImmutableConfiguration;

import java.util.Iterator;

/**
 * @author Michael Fortin
 */
public class InfoCmd implements Runnable {
    String[] name;
    DataStore dataStore;

    public InfoCmd(DataStore dataStore, String[] name) {
        this.name = name;
        this.dataStore = dataStore;
    }

    @Override
    public void run() {
        if (name.length == 2) {
            dataStore.findByName(name[1]).ifPresent(this::outputConfig);
        }
    }

    private void outputConfig(ImmutableConfiguration config) {
        final Iterator<String> keys = config.getKeys();
        while (keys.hasNext()) {
            String next = keys.next();
            System.out.println("  " + next + ": " + config.getString(next));
        }
    }
}
