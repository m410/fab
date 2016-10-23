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
package org.m410.fabricate.garden;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Generates a web.xml and configuration file from the source.
 * http://docs.codehaus.org/display/MAVENUSER/Mojo+Developer+Cookbook#MojoDeveloperCookbook-Foraccessingartifactsandrepositories
 *
 * @author Michael Fortin
 */
public final class WebXmlTask implements Task {

    private static final String xml =
            "<web-app xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd\"\n" +
            "         version=\"3.1\"\n" +
            "         metadata-complete=\"true\">\n" +
            "  <display-name>{{disName}}</display-name>\n" +
            "  <context-param>\n" +
            "    <param-name>m410-env</param-name>\n" +
            "    <param-value>{{env}}</param-value>\n" +
            "  </context-param>\n" +
            "  <listener>\n" +
            "    <listener-class>org.m410.garden.application.ApplicationContextListener</listener-class>\n" +
            "  </listener>\n" +
            "</web-app>";


    @Override
    public String getName() {
        return "web-xml";
    }

    @Override
    public String getDescription() {
        return "Generate the web.xml configuration file";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        final String compile = context.getClasspath().get("compile");
        Collection<File> mavenProject = Arrays.stream(compile.split(System.getProperty("path.separator")))
                .map(File::new)
                .collect(Collectors.toList());

        context.cli().debug("artifacts =" + mavenProject    );
        final String webOut = context.getConfiguration().getString("build.webappOutput");
        initWebXml(webOut, context.environment(), context.getApplication().getName());
        moveM410Config(context.getBuild().getSourceOutputDir());
    }

    private void moveM410Config(String sourceOut) throws IOException {
        // todo should e the cached env version
        // todo fix hardcode paths
        Path source = Paths.get("garden.fab.yml");
        Path target = Paths.get("target/classes/garden.fab.yml");

        if(!target.toFile().exists())
            Files.copy(source, target);
    }

    private void initWebXml(String webappDir, String envName, String appName) throws IOException {
        File outputDir = FileSystems.getDefault().getPath(webappDir, "WEB-INF").toFile();

        if(!outputDir.exists() && !outputDir.mkdirs())
            throw new RuntimeException("Could not create web-inf directory");

        final File webxml = new File(outputDir, "web.xml");
        final String xmlOut = xml.replace("{{env}}", envName).replace("{{disName}}", appName);

        try(FileWriter writer = new FileWriter(webxml)) {
            writer.write(xmlOut);
        }
    }
}
