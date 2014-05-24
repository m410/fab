package org.m410.fab.loader.ivy;

import org.m410.fab.builder.BuildContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

/**
 * @author m410
 */
public class IvySettingBuilder {


//    <ivysettings>
//    <property name="revision" value="SNAPSHOT" override="false"/>
//    <property name="ivy.checksums" value=""/>
//    <settings defaultResolver="default"/>
//    <caches defaultCacheDir="${user.home}/.fab/ivy-cache" />
//    <resolvers>
//    <ibiblio name="maven-local" root="file://${user.home}/.m2/repository" m2compatible="true" />
//    {for(repo <- repos; if(repo.id.isDefined && repo.url.isDefined)) yield
//            <ibiblio name={repo.id.get} root={repo.url.get} m2compatible="true"  />
//    }
//    <filesystem name="pub-mvn-local" m2compatible="true">
//    <artifact pattern="${user.home}/.m2/repository/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]"/>
//    </filesystem>
//    <filesystem name="pub-fab-local">
//    <ivy pattern="${user.home}/.fab/ivy-cache/[organisation]/[module]/[artifact]-[revision](-[classifier]).[ext]" />
//    <artifact pattern="${user.home}/.fab/ivy-cache/[organisation]/[module]/[type]s/[artifact]-[revision](-[classifier]).[ext]" />
//    </filesystem>
//    <chain name="default" returnFirst="true">
//    {for(repo <- repos;if(repo.id.isDefined && repo.url.isDefined)) yield
//            <resolver ref={repo.id.get} />
//    }
//    <resolver ref="maven-local"/>
//    </chain>
//    </resolvers>
//    </ivysettings>

    private IvySettingBuilder() {
    }

    private String makeXml() throws ParserConfigurationException, TransformerException {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        final Document doc = docBuilder.newDocument();
        final Element root = doc.createElement("ivysettings");
        doc.appendChild(root);

        final Element property = doc.createElement("property");
        property.setAttribute("name","revision");
        property.setAttribute("value","SNAPSHOT");
        property.setAttribute("override","false");
        root.appendChild(property);
        // todo fill me in

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);


        StringWriter s = new StringWriter();
        StreamResult result = new StreamResult(s);
        transformer.transform(source, result);
        return s.toString();
    }

    public static IvySettingBuilder with(BuildContext context) {
        return new IvySettingBuilder();
    }

    public File build() {
        return new File("");
    }
}
