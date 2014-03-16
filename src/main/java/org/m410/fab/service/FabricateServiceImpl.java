package org.m410.fab.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public class FabricateServiceImpl implements FabricateService {
    private List<String> tasks = new ArrayList<>();

    @Override
    public void addCommand(String c) {
        tasks.add("cmd:" + c);
    }

    @Override
    public void addTask(String c) {
        tasks.add("task:"+c);
    }

    @Override
    public void addConfiguration(String config) {
    }

    @Override
    public void execute(String[] taskList) {
        for (String s : tasks)
            System.out.println("TASK:"+s);

    }

    @Override
    public String toString() {
        return "FabricateServiceImp["+tasks+"]";
    }
}
