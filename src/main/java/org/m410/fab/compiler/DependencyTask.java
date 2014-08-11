package org.m410.fab.compiler;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class DependencyTask implements Task {
    @Override
    public String getName() {
        return "compile task";
    }

    @Override
    public String getDescription() {
        return "Download dependencies";
    }

    @Override
    public void execute(BuildContext context) {
        // todo add ivy code here to download dependencies, see IvyXml & IvySettingsXml.xml

/*
 <ivy-module version="2.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation= "http://ant.apache.org/ivy/schemas/ivy.xsd"
    xmlns:m="http://ant.apache.org/ivy/maven">
  <info module={name} organisation={org} revision={version}/>
  <configurations>
    <conf name="default" />
  </configurations>
  <dependencies defaultconfmapping="*->*" defaultconf="default">
  {if(config.modules.isDefined)
    for(mod <-config.modules.get) yield
    <dependency org={mod.org.get} name={mod.name.get} rev={mod.version.get} transitive="false">
      <artifact name={mod.name.get} type="jar" m:classifier="module" />
    </dependency>
  }
  {if(config.persistence.isDefined)
    for(psst <- config.persistence.get) yield
    <dependency org={psst.org.get} name={psst.name.get} rev={psst.version.get} transitive="false">
      <artifact name={psst.name.get} type="jar" m:classifier="module" />
    </dependency>
  }
  {if(config.views.isDefined){
    val view = config.views.get
    <dependency org={view.org.get} name={view.name.get} rev={view.version.get} transitive="false">
      <artifact name={view.name.get} type="jar" m:classifier="module" />
    </dependency>
  }}
  </dependencies>
</ivy-module>






<ivysettings>
  <property name="revision" value="SNAPSHOT" override="false"/>
  <property name="ivy.checksums" value=""/>
  <settings defaultResolver="default"/>
  <caches defaultCacheDir="${user.home}/.fab/ivy-cache" />
  <resolvers>
    <ibiblio name="maven-local" root="file://${user.home}/.m2/repository" m2compatible="true" />
    {for(repo <- repos; if(repo.id.isDefined && repo.url.isDefined)) yield
    <ibiblio name={repo.id.get} root={repo.url.get} m2compatible="true"  />
    }
    <filesystem name="pub-mvn-local" m2compatible="true">
      <artifact pattern="${user.home}/.m2/repository/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]"/>
    </filesystem>
    <filesystem name="pub-fab-local">
      <ivy pattern="${user.home}/.fab/ivy-cache/[organisation]/[module]/[artifact]-[revision](-[classifier]).[ext]" />
      <artifact pattern="${user.home}/.fab/ivy-cache/[organisation]/[module]/[type]s/[artifact]-[revision](-[classifier]).[ext]" />
    </filesystem>
    <chain name="default" returnFirst="true">
      {for(repo <- repos;if(repo.id.isDefined && repo.url.isDefined)) yield
      <resolver ref={repo.id.get} />
      }
      <resolver ref="maven-local"/>
    </chain>
  </resolvers>
</ivysettings>
 */




        context.cli().debug("need to load dependencies here");
    }
}
