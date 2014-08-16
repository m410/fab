package org.m410.fab.garden;

//import org.apache.catalina.Context;
//import org.apache.catalina.Server;
//import org.apache.catalina.startup.Tomcat;
import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.File;

/**
 * @author m410
 */
public class TomcatReloadingServerTask implements Task {

    @Override
    public String getName() {
        return "Reloading Tomcat Server";
    }

    @Override
    public String getDescription() {
        return "Reloading Tomcat Server";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
//        Tomcat tomcat = new Tomcat();
//        tomcat.setPort(8080);
//        Context webapp = tomcat.addWebapp("/", new File("webapp").getAbsolutePath());
//        tomcat.start();
//        tomcat.getServer().await();
    }
}
