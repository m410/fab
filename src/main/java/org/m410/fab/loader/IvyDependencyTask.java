package org.m410.fab.loader;

import org.apache.ivy.Ivy;
import org.apache.ivy.util.MessageLoggerEngine;
import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;
import org.m410.fab.loader.ivy.DependencyScope;
import org.m410.fab.loader.ivy.IvyConfigBuilder;
import org.m410.fab.loader.ivy.IvySettingBuilder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * @author m410
 */
public class IvyDependencyTask implements Task {
    public static final String NAME = "dependency-task";
    public static final String DESC = "Resolve dependencies using Ivy";

    private final DependencyScope dependencyScope;

    public IvyDependencyTask(DependencyScope dependencyScope) {
        this.dependencyScope = dependencyScope;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public void execute(BuildContext buildContext) {

        Ivy ivy = new Ivy() {
            @Override
            public MessageLoggerEngine getLoggerEngine() {
                return new MessageLoggerEngine(){
//                    override def rawinfo(p1: String) {messenger.debug(rmTab(p1))}
//                    override def deprecated(p1: String) {messenger.warn(rmTab(p1))}
//                    override def verbose(p1: String) {messenger.debug(rmTab(p1))}

                    @Override
                    public void error(String msg) {
                        super.error(msg);
                    }

                    @Override
                    public void warn(String msg) {
                        super.warn(msg);
                    }

                    @Override
                    public void debug(String msg) {
                        super.debug(msg);
                    }

                    @Override
                    public boolean isShowProgress() {
                        return false;
                    }

                    @Override
                    public void info(String msg) {
                        super.info(msg);
                    }
                };
            }

        };
        ivy.bind();

        File settingsFile = IvySettingBuilder.with(buildContext).build();
        File ivyFile = IvyConfigBuilder.with(buildContext).build();

        ivy.execute((ivy1, ivyContext) -> {

            try {
                ivy1.configure(settingsFile);
                ivy1.getResolveEngine().resolve(ivyFile);
            }
            catch (ParseException | IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
