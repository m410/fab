package org.m410.fabricate.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author m410
 */
public final class Command implements Serializable{
    private final String name;
    private final String description;
    private final boolean takesArgs;
    private final List<Step> steps;

    public Command(String name, String description, boolean takesArgs) {
        this.name = name;
        this.description = description;
        this.takesArgs = takesArgs;
        this.steps = new ArrayList<>();
    }

    public Command(String name, String description, boolean takesArgs, List<Step> steps) {
        this.name = name;
        this.description = description;
        this.takesArgs = takesArgs;
        this.steps = steps;
    }

    public Command withStep(Step step) {
        steps.add(step);
        return this;
    }

    public Step lastStep() {
        if(steps.size() > 0)
            return steps.get(steps.size() - 1);
        else
            return null;
    }

    public Step firstStep() {
        if(steps.size() > 0)
            return steps.get(0);
        else
            return null;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public void execute(List<TaskListener> taskListeners, BuildContext buildContext) throws Exception {
        for (Step step : steps) {
            for (Task task : step.getTasks()) {
                for (TaskListener taskListener : taskListeners) {
                    taskListener.notify(new TaskEvent(task));
                }
                task.execute(buildContext);
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public boolean isTakesArgs() {
        return takesArgs;
    }
}
