package org.m410.fab.javalib;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;
import org.m410.fab.config.Dependency;
import org.m410.fab.config.Module;

import java.util.stream.Collectors;

/**
 * @author m410
 */
public class BuildTask implements Task {
    @Override
    public String getName() {
        return "build task";
    }

    @Override
    public String getDescription() {
        return "Build the project";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        context.cli().debug(context.environment());
        context.cli().debug(context.application().toString());
        context.cli().debug(context.build().toString());
        context.cli().debug(context.dependencies().stream().map(Dependency::toString).collect(Collectors.joining(",")));
        context.cli().debug(context.modules().stream().map(Module::toString).collect(Collectors.joining(",")));
        context.cli().debug("BUILDING BITCHES!!!");
    }
}