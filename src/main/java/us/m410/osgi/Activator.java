package us.m410.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        // TODO add activation code here
        System.out.println("start with " + context);
    }

    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
        System.out.println("stop with " + context);
    }

}
