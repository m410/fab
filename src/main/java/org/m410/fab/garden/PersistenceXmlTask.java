/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Uses the persistence defintion from the configuration.m410.xml to create the
 * configuration file.
 *
 * @author Michael Fortin
 */
public final class PersistenceXmlTask implements Task {

    final String builderName = "org.m410.garden.module.ormbuilder.persistence.PersistenceXmlBuilder";


    @Override
    public String getName() {
        return "persistence-xml";
    }

    @Override
    public String getDescription() {
        return "Generate the persistence.xml configuration file";
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

        final Path outputPath = FileSystems.getDefault().getPath("target/classes/META-INF/persistence.xml");

        new ReflectConfigFileBuilder(builderName)
                .withClasspath(mavenProject)
                .withPath(outputPath)
                .withEnv(context.environment())
                .make();
    }
}
