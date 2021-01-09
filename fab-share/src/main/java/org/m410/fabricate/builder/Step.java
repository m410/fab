package org.m410.fabricate.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author m410
 */
public final class Step  implements Serializable {
    private final String name;
    private final List<Task> tasks;

    public Step(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public Step(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public Step append(Task task) {
        tasks.add(task);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
