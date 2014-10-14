package org.m410.fab.garden.jetty;

import org.m410.fab.garden.jetty.ReloadingEvent;

import java.util.EventListener;

/**
* @author m410
*/
public interface ReloadingEventListener extends EventListener {
    public void onChange(ReloadingEvent reloadingEvent);
}
