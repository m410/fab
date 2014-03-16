package org.m410.fab.service;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface FabricateService {

    void addCommand(String c);

    void addTask(String c);

    void execute(String[] taskList);

    void addConfiguration(String config);
}
