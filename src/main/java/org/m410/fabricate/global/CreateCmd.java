package org.m410.fabricate.global;

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
        System.out.println("create it");
    }
}
