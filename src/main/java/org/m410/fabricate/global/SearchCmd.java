package org.m410.fabricate.global;

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
        System.out.println("find it");
    }
}

