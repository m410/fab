package org.m410.fabricate.global;

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
        System.out.println("describe it");
    }
}
