package org.m410.fabricate.dependency;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.File;
import java.util.Arrays;

/**
 * @author Michael Fortin
 */
public class MavenResolverTask implements Task {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        //  https://github.com/shrinkwrap/resolver
        //        String coordinate = "commons-fileupload:commons-fileupload:1.3.2";
        String coordinate = "org.m410.fabricate:garden:0.2-SNAPSHOT";

        File[] files = Maven.configureResolver()
                .withClassPathResolution(false)
                .withMavenCentralRepo(false)
                .withRemoteRepo("m410-snapshot", "http://repo.m410.org/content/repositories/snapshots/", "default")
                .resolve(coordinate)
                .withTransitivity()
                .asFile();

        Arrays.stream(files).forEach(f -> System.out.println(f.getAbsolutePath()));
    }
}
