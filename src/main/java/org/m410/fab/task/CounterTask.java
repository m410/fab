package org.m410.fab.task;


import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class CounterTask implements Task {
    private final int count;

    public CounterTask(int count) {
        this.count = count;
    }

    @Override
    public String getName() {
        return "count-" + count;
    }

    @Override
    public String getDescription() {
        return "Counter " + count;
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().debug("count-" + count);
    }
}
