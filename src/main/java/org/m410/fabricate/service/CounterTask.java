package org.m410.fabricate.service;


import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

/**
 * @author m410
 */
@Deprecated
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
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("count-" + count);
    }
}
