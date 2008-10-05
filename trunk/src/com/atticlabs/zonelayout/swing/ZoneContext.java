package com.atticlabs.zonelayout.swing;

import java.util.HashMap;
import java.util.Map;

final class ZoneContext {
    Map context = new HashMap();

    boolean containsZone(String id) {
        return context.containsKey(id);
    }

    Zone getZone(String id) {
        return (Zone) context.get(id);
    }

    void addZone(Zone zone) {
        context.put(zone.getId(), zone);
    }

    Map getZones() {
        return context;
    }
}
