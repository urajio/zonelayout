package com.atticlabs.zonelayout.swing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

final class Template {
    AxisView view = AxisView.Y;
    String name;
    Point insertionPoint = new Point();
    Point endPoint = new Point();
//    protected List sections = new ArrayList();
    Set zones = new HashSet();
    int count;
    ZoneContext lastContext;

    Template(String name, int insertionPoint) {
        this.name = name;
        view.setValue(this.insertionPoint, insertionPoint);
        view.setValue(this.endPoint, insertionPoint);
    }

    void incrementEndPoint() {
        view.adjust(endPoint, 1);
    }

    String getName() {
        return name;
    }

    Point getInsertionPoint() {
        return insertionPoint;
    }

    Point getEndPoint() {
        return endPoint;
    }

    AxisView getView() {
        return view;
    }

    void addZone(Zone zone) {
        // we need to reset the zones coordinate system to the template coordinate system.
        view.adjust(zone.getRoot(), -1 * view.getValue(insertionPoint));
        view.adjust(zone.getSecondRoot(), -1 * view.getValue(insertionPoint));
        zones.add(zone);
    }

    Set getZones() {
        return zones;
    }

//    public List getSections() {
//        return sections;
//    }

    ZoneContext getNewZoneContext() {
        ZoneContext context = new ZoneContext();
        for (Iterator iterator = zones.iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            Zone newZone = new Zone(zone);

            view.adjust(newZone.getRoot(), view.getValue(insertionPoint));
            view.adjust(newZone.getSecondRoot(), view.getValue(insertionPoint));

            context.addZone(newZone);
        }
        return context;
    }

    ZoneContext getLastContext() {
        return lastContext;
    }

    void setLastContext(ZoneContext lastContext) {
        this.lastContext = lastContext;
    }

    void setLastContextZoneIds() {
        List values = new ArrayList(lastContext.getZones().values());
        for (int i = 0; i < values.size(); i++) {
            Zone zone = (Zone) values.get(i);
            zone.setId(zone.getId() + "-" + count);
        }
        lastContext.getZones().clear();
        for (int i = 0; i < values.size(); i++) {
            Zone zone = (Zone) values.get(i);
            lastContext.addZone(zone);
        }
        count++;
    }
}
