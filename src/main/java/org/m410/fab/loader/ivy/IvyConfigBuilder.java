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
public class IvyConfigBuilder {

//    <ivy-module version="2.0"
//    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//    xsi:noNamespaceSchemaLocation="http://ant.apache.org/ivy/schemas/ivy.xsd"
//    xmlns:m="http://ant.apache.org/ivy/maven">
//    <info module={config.application.get.name.get} organisation={config.application.get.org.get} revision={config.application.get.version.get} />
//    <configurations>
//    <conf name="provided"/>
//    <conf name="compile"/>
//    <conf name="test"/>
//    <conf name="runtime"/>
//    </configurations>
//    <publications>
//    <artifact type="pom" ext="pom" conf="compile"/>
//    <artifact type="jar" ext="jar" conf="compile"/>
//    </publications>
//    <dependencies defaultconfmapping="*->*" defaultconf="compile,sources,javadoc">
//    {for(dp <- config.dependencies.toList) yield {
//        <dependency org={dp.org.getOrElse("ERROR")} name={dp.name.getOrElse("ERROR")} rev={dp.rev.getOrElse("ERROR")} conf={dp.conf.getOrElse("ERROR")} transitive={dp.transitive.getOrElse(true).toString}>
//        <artifact name={dp.name.getOrElse("ERROR")} type="jar" />
//                {if(dp.excludes.isDefined) for(exclude <- dp.excludes.get) yield {
//            <exclude org={exclude.org.getOrElse("ERROR")} name={exclude.name.getOrElse("ERROR")} conf={dp.conf.getOrElse("ERROR")}/>
//        }}
//        </dependency>
//    }}
//    {for(exclude <- config.dependencyExcludes.toList) yield
//            <exclude org={exclude.org.getOrElse("ERROR")} artifact={exclude.name.getOrElse("ERROR")} />
//    }
//    </dependencies>
//    </ivy-module>
//

    private IvyConfigBuilder() {
    }

    private String makeXml() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element root = doc.createElementNS("http://xmlns.jcp.org/xml/ns/persistence", "persistence");
        root.setAttribute("version", "2.1");
        root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation",
                "http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd");
        doc.appendChild(root);

        Element persistUnit = doc.createElement("persistence-unit");
        persistUnit.setAttribute("transaction-type","RESOURCE_LOCAL");

        Element provider = doc.createElement("provider");
        provider.setTextContent("org.hibernate.jpa.HibernatePersistenceProvider");
        persistUnit.appendChild(provider);

        Element propertiesElem = doc.createElement("properties");

        persistUnit.appendChild(propertiesElem);
        root.appendChild(persistUnit);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);


        // validation
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL schemaURL = new URL("http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd");
            Schema schema = sf.newSchema(schemaURL);
            Validator validator = schema.newValidator();
            validator.validate(source);
        }
        catch (SAXException | IOException e) {
            System.out.println("WARNING: Did not validate: " + e.getMessage());
        }


        StringWriter s = new StringWriter();
        StreamResult result = new StreamResult(s);
        transformer.transform(source, result);
        return s.toString();
    }

    public static IvyConfigBuilder with(BuildContext context) {
        return new IvyConfigBuilder();
    }

    public File build() {
        return new File("");
    }
}
