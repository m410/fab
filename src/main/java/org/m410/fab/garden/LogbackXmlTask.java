package org.m410.fab.garden;


import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Creates the logback.xml file from the configuration.m410.xml.
 *
 * @author Michael Fortin
 */
public final class LogbackXmlTask implements Task{

    final String builderName = "org.m410.garden.configuration.LogbackXmlBuilder";


    @Override
    public String getName() {
        return "logback-xml";
    }

    @Override
    public String getDescription() {
        return "generate the logback xml configuration file";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        Collection<File> mavenProject = Arrays.asList(
                context.getClasspath()
                        .get("compile")
                        .split(System.getProperty("path.separator")))
                .stream()
                .map(File::new)
                .collect(Collectors.toList());

        final String sourceDir = context.getBuild().getSourceOutputDir();
        final Path outputPath = FileSystems.getDefault().getPath(sourceDir, "logback.xml");

        new ReflectConfigFileBuilder(builderName)
                .withClasspath(mavenProject)
                .withPath(outputPath)
                .withEnv(context.environment())
                .make();
    }

}
