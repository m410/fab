package org.m410.fabricate.dependency;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.plugins.report.XmlReportParser;
import org.apache.ivy.util.MessageLoggerEngine;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;
import org.m410.fabricate.config.Dependency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author m410
 */
public class IvyDependencyTask implements Task {
    @Override
    public String getName() {
        return "compile task";
    }

    @Override
    public String getDescription() {
        return "Download dependencies";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        File ivyFile = makeIvyXml(context);
        File ivySettingFile = makeIvySettingsXml(context);
        resolveDependencies(context, ivySettingFile, ivyFile);
    }

    void resolveDependencies(BuildContext context, File settingsFile, File ivyFile) {
        Ivy ivy = new Ivy() {
            @Override
            public MessageLoggerEngine getLoggerEngine() {
                return new MessageLoggerEngine(){
                    @Override public void warn(String msg) { context.cli().warn(msg); }
                    @Override public void error(String msg) { context.cli().error(msg); }
                    @Override public void info(String msg) { context.cli().debug(msg); }
                    @Override public void debug(String msg) { context.cli().debug(msg); }
                    @Override public void verbose(String msg) { context.cli().debug(msg); }
                    @Override public void log(String msg, int level) { context.cli().debug(msg); }
                };
            }
        };

        final List<String> scopes = context.getDependencies().stream()
                .map(Dependency::getScope).sorted().collect(Collectors.toList());
        scopes.add("sources");
        scopes.add("javadoc");

        ivy.execute((ivy1, ivyContext) -> {
            Map<String,List<ArtifactDownloadReport>> reports = new HashMap<String, List<ArtifactDownloadReport>>();

            try {
                ivy1.configure(settingsFile);
                ResolveReport resolveReport = ivy1.getResolveEngine().resolve(ivyFile);
                String resolveId = resolveReport.getResolveId();
                ResolutionCacheManager manager = ivy1.getResolutionCacheManager();

                scopes.forEach(s -> reports.put(s, Arrays.asList(makeReport(s, resolveId, manager))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            final String pathSeparator = System.getProperty("path.separator");

            for (String s : reports.keySet()) {
                StringBuilder sb = new StringBuilder();
                reports.get(s).stream()
                        .map(ArtifactDownloadReport::getLocalFile)
                        .filter(f -> f != null)
                        .forEach(f -> {
                            sb.append(f.getAbsolutePath());
                            sb.append(pathSeparator);
                        });

                context.getClasspath().put(s, sb.toString());
            }
            return null;
        });
    }

    private ArtifactDownloadReport[] makeReport(String conf, String resolveId, ResolutionCacheManager manager) {
        File reportCompile = manager.getConfigurationResolveReportInCache(resolveId, conf);
        XmlReportParser parser = new XmlReportParser();

        try {
            parser.parse(reportCompile);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return parser.getArtifactReports();
    }

    File makeIvyXml(final BuildContext context) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("ivy-module");
        rootElement.setAttribute("version", "2.0");

        doc.appendChild(rootElement);

        Element infoElement = doc.createElement("info");
        infoElement.setAttribute("module",context.getApplication().getName());
        infoElement.setAttribute("organisation",context.getApplication().getOrg());
        infoElement.setAttribute("revision",context.getApplication().getVersion());
        rootElement.appendChild(infoElement);

        Element configurationElement = doc.createElement("configurations");

        final Element confElement1 = doc.createElement("conf");
        confElement1.setAttribute("name","default");
        confElement1.setAttribute("visibility","public");
        configurationElement.appendChild(confElement1);

        final Element confElement0 = doc.createElement("conf");
        confElement0.setAttribute("name","provided");
        confElement0.setAttribute("extends","default");
        confElement0.setAttribute("visibility","public");
        configurationElement.appendChild(confElement0);

        final Element confElement2 = doc.createElement("conf");
        confElement2.setAttribute("name","compile");
        confElement2.setAttribute("visibility","public");
        confElement2.setAttribute("extends","provided");
        configurationElement.appendChild(confElement2);

        final Element confElement3 = doc.createElement("conf");
        confElement3.setAttribute("name","test");
        confElement3.setAttribute("visibility","public");
        confElement3.setAttribute("extends","compile");
        configurationElement.appendChild(confElement3);

        final Element confElement4 = doc.createElement("conf");
        confElement4.setAttribute("name","javadoc");
        confElement4.setAttribute("visibility","public");
        configurationElement.appendChild(confElement4);

        final Element confElement5 = doc.createElement("conf");
        confElement5.setAttribute("name","sources");
        confElement5.setAttribute("visibility","public");
        configurationElement.appendChild(confElement5);

        // todo add non standard configs like jetty9
        context.getDependencies().stream()
                .filter(d->!d.getScope().equals("runtime"))
                .filter(d->!d.getScope().equals("compile"))
                .filter(d->!d.getScope().equals("provided"))
                .filter(d->!d.getScope().equals("sources"))
                .filter(d->!d.getScope().equals("javadoc"))
                .filter(d->!d.getScope().equals("test"))
                .map(Dependency::getScope)
                .sorted()
                .forEach(scope -> {
                    final Element e = doc.createElement("conf");
                    e.setAttribute("name", scope);
                    e.setAttribute("visibility", "public");
                    configurationElement.appendChild(e);
                });

        rootElement.appendChild(configurationElement);

        Element pubElem = doc.createElement("publications");

        final Element artifactElem = doc.createElement("artifact");
        artifactElem.setAttribute("type","pom");
        artifactElem.setAttribute("ext","pom");
        artifactElem.setAttribute("conf","compile");
        pubElem.appendChild(artifactElem);

        final Element artifactElem2 = doc.createElement("artifact");
        artifactElem2.setAttribute("type","jar");
        artifactElem2.setAttribute("ext","jar");
        artifactElem2.setAttribute("conf","compile");
        pubElem.appendChild(artifactElem2);

        rootElement.appendChild(pubElem);

        Element dependenciesElement = doc.createElement("dependencies");
        rootElement.appendChild(dependenciesElement);

        for (Dependency dependency : context.getDependencies()) {
            Element dependencyElem = doc.createElement("dependency");
            dependencyElem.setAttribute("org", dependency.getOrg());
            dependencyElem.setAttribute("name", dependency.getName());
            dependencyElem.setAttribute("rev", dependency.getRev());
            dependencyElem.setAttribute("transitive", Boolean.toString(dependency.isTransitive()));
            dependencyElem.setAttribute("conf", dependency.getScope()+"->default;");

            // todo figure out downloading source javadoc later
//            dependencyElem.setAttribute("conf", dependency.getScope()+"->default;sources->sources;javadoc->javadoc");
            dependenciesElement.appendChild(dependencyElem);
        }

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        final DOMSource source = new DOMSource(doc);
        final String out = context.getConfiguration().getString("build.cache_dir");
        final File file = FileSystems.getDefault().getPath(out, "ivy.xml").toFile();
        final StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        return file;
    }

    File makeIvySettingsXml(BuildContext context) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("ivysettings");
        doc.appendChild(rootElement);

        Element prop1 = doc.createElement("property");
        prop1.setAttribute("name","revision");
        prop1.setAttribute("value","SNAPSHOT");
        prop1.setAttribute("override","false");
        rootElement.appendChild(prop1);

        Element prop2 = doc.createElement("property");
        prop2.setAttribute("name","ivy.checksums");
        prop2.setAttribute("value","");
        rootElement.appendChild(prop2);

        Element settings = doc.createElement("settings");
        settings.setAttribute("defaultResolver","default-chain");
        rootElement.appendChild(settings);

        Element caches = doc.createElement("caches");
        caches.setAttribute("defaultCacheDir","${user.home}/.fab/repository");
        rootElement.appendChild(caches);

        Element resolvers = doc.createElement("resolvers");
        Element chain = doc.createElement("chain");
        chain.setAttribute("name","default-chain");

        Element ibiblio = doc.createElement("ibiblio");
        ibiblio.setAttribute("name","maven-local");
        ibiblio.setAttribute("root","file://${user.home}/.m2/repository");
        ibiblio.setAttribute("m2compatible","true");
        chain.appendChild(ibiblio);

        Element ibiblioCentral = doc.createElement("ibiblio");
        ibiblioCentral.setAttribute("name","ibiblio");
        ibiblioCentral.setAttribute("m2compatible","true");
        chain.appendChild(ibiblioCentral);

        Element mavenCentral = doc.createElement("ibiblio");
        mavenCentral.setAttribute("name","maven-central");
        mavenCentral.setAttribute("root","http://repo1.maven.org/maven2/");
        mavenCentral.setAttribute("m2compatible","true");
        chain.appendChild(mavenCentral);

        resolvers.appendChild(chain);

        // todo add configured repositories

        rootElement.appendChild(resolvers);

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        final DOMSource source = new DOMSource(doc);
        final String out = context.getConfiguration().getString("build.cache_dir");
        final File file = FileSystems.getDefault().getPath(out, "ivy-settings.xml").toFile();
        final StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        return file;
    }
}
