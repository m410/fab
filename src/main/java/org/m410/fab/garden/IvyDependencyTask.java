package org.m410.fab.garden;

import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.plugins.report.XmlReportParser;
import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.m410.fab.config.Dependency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import org.apache.ivy.Ivy;
import org.apache.ivy.util.MessageLoggerEngine;


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
        // generate ivy.xml
        File ivyFile = makeIvyXml(context);
        // generate ivy-setting.xml
        File ivySettingFile = makeIvySettingsXml(context);
        // create read ivy default settings in ~/.fab
        // call ivy to pull resources to local repository in ~/.fab/dependencies
        resolveDependencies(context, ivySettingFile, ivyFile);

        // create environment classpaths and add them to the context
        // somehow compare base configuration to know if reloading is required
        // write out classpaths to cache to quick access on second run
    }

    void resolveDependencies(BuildContext context, File settingsFile, File ivyFile) {
        Ivy ivy = new Ivy() {
            @Override
            public MessageLoggerEngine getLoggerEngine() {
                return new MessageLoggerEngine(){
                    @Override public void warn(String msg) { context.cli().warn(msg); }
                    @Override public void error(String msg) { context.cli().error(msg); }
                    @Override public void info(String msg) { context.cli().info(msg); }
                    @Override public void debug(String msg) { context.cli().debug(msg); }
                };
            }
        };

        final Object result = ivy.execute((ivy1, ivyContext) -> {
            ArtifactDownloadReport[] reports = new ArtifactDownloadReport[0];

            try {
                ivy1.configure(settingsFile);
                ResolveReport resolveReport = ivy1.getResolveEngine().resolve(ivyFile);
                String resolveId = resolveReport.getResolveId();

                ResolutionCacheManager manager = ivy1.getResolutionCacheManager();
                final File reportCompile = manager.getConfigurationResolveReportInCache(resolveId, "compile");
                XmlReportParser parser = new XmlReportParser();
                parser.parse(reportCompile);
                reports = parser.getArtifactReports();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            return Arrays.asList(reports).stream()
                    .map(ArtifactDownloadReport::getLocalFile)
                    .collect(Collectors.toList());
        });

	// todo need to add provided, compile, and test to classpaths

        StringBuilder sb = new StringBuilder();
        ((List<File>)result).stream().forEach(f->{
            sb.append(f.getAbsolutePath()).append(System.getProperty("path.separator"));
        });

        context.classpaths().put("compile", sb.toString());
    }

    File makeIvyXml(final BuildContext context) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("ivy-module");
        rootElement.setAttribute("version", "2.0");
        rootElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation",
                "http://ant.apache.org/ivy/schemas/ivy.xsd");

        doc.appendChild(rootElement);

        Element infoElement = doc.createElement("info");
        infoElement.setAttribute("module",context.application().getName());
        infoElement.setAttribute("organisation",context.application().getOrg());
        infoElement.setAttribute("revision",context.application().getVersion());
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
		//dependenciesElement.setAttribute("defaultconfmapping","*->*");
		//dependenciesElement.setAttribute("defaultconf","compile,sources,javadoc");
        rootElement.appendChild(dependenciesElement);

        for (Dependency dependency : context.dependencies()) {
            Element dependencyElem = doc.createElement("dependency");
            dependencyElem.setAttribute("org", dependency.getOrg());
            dependencyElem.setAttribute("name", dependency.getName());
            dependencyElem.setAttribute("rev", dependency.getRev());
            dependencyElem.setAttribute("transitive", "false");
            dependencyElem.setAttribute("conf", dependency.getScope());

			// conf = javadoc->javadoc;sources->sources;compile->default

            // Element artifact = doc.createElement("artifact");
            //artifact.setAttribute("name",dependency.getName());
            //artifact.setAttribute("type","jar");
            // artifact.setAttribute("m:classifier","module");
            //dependencyElem.appendChild(artifact);

            dependenciesElement.appendChild(dependencyElem);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = FileSystems.getDefault().getPath(context.build().getCacheDir(),"ivy.xml").toFile();
        StreamResult result = new StreamResult(file);
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

        Element prop3 = doc.createElement("property");
        prop3.setAttribute("name","m2-pattern");
//        prop3.setAttribute("value","${user.home}/.m2/repository/[organisation]/[module]/[revision]/[module]-[revision](-[classifier]).[ext]");
        prop3.setAttribute("override","false");
        rootElement.appendChild(prop3);

   
        Element settings = doc.createElement("settings");
        settings.setAttribute("defaultResolver","default-chain");
        rootElement.appendChild(settings);

        Element caches = doc.createElement("caches");
        caches.setAttribute("defaultCacheDir","${user.home}/.fab/repository");
        rootElement.appendChild(caches);

        Element resolvers = doc.createElement("resolvers");
        Element chain = doc.createElement("chain");
        chain.setAttribute("name","default-chain");
//
//        Element m2Local = doc.createElement("filesystem");
//		m2Local.setAttribute("name","m2-local");
//		m2Local.setAttribute("m2compatible","true");
//        Element m2Artifact = doc.createElement("artifact");
//		m2Artifact.setAttribute("pattern","${m2-pattern}");
//		m2Local.appendChild(m2Artifact);
//        Element m2ivy = doc.createElement("ivy");
//		m2ivy.setAttribute("pattern","${m2-pattern}");
//		m2Local.appendChild(m2ivy);
//		chain.appendChild(m2Local);
//
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

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
        File file = FileSystems.getDefault().getPath(context.build().getCacheDir(),"ivy-settings.xml").toFile();
        StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
        return file;
    }
}
