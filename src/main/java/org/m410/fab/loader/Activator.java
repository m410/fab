package org.m410.fab.loader;

import org.m410.fab.service.FabricateService;
import org.m410.fab.service.FabricateServiceImpl;
import org.osgi.framework.*;

/**
 *
 * @author Michael Fortin
 */
public class Activator implements BundleActivator {
    ServiceRegistration fabricateServiceRegistration;

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        FabricateService fabricateService = new FabricateServiceImpl();
        final String name = FabricateService.class.getName();
        fabricateServiceRegistration = context.registerService(name, fabricateService, null);
    }

    public void stop(BundleContext context) throws Exception {
        fabricateServiceRegistration.unregister();
    }

}
