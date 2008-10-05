package com.atticlabs.zonelayout.swing;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

abstract class Section {
    ZoneLayoutImpl layout;
    Point root = new Point();
    int preferredSize;
    int minimumSize;
    int size;
    double take;
    double give;
    Point displayRoot = new Point();

    Section(ZoneLayoutImpl layout, int coordinate) {
        this.layout = layout;
        setCoordinate(coordinate);
    }

    Section(Section section, ZoneContext context) {
        this.layout = section.layout;
        this.root = new Point(section.root);
        this.preferredSize = section.preferredSize;
        this.minimumSize = section.minimumSize;
        this.size= section.size;
        this.take = section.take;
        this.give = section.give;
        this.displayRoot = new Point(section.displayRoot);

    }

    Point getRoot() {
        return root;
    }

    Set getZones() {
        Set zones = new HashSet();

        for (Iterator iterator = layout.getZones().values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (zone.intersectsAxisLine(getView(), root)) {
                zones.add(zone);
            }
        }

        return zones;
    }

    public double getTake() {
        return take;
    }

    public void setTake(double take) {
        this.take = take;
    }

    public double getGive() {
        return give;
    }

    public void setGive(double give) {
        this.give = give;
    }

    boolean intersectsWithZone(Zone zone) {
        return getView().contains(zone, getCoordinate());
    }

    void setCoordinate(int coordinate) {
        getView().setValue(root, coordinate);
    }

    int getCoordinate() {
        return getView().getValue(root);
    }

    int getPreferredSize() {
        return preferredSize;
    }

    void setPreferredSize(int prefSize) {
        this.preferredSize = prefSize;
    }

    int getMinimumSize() {
        return minimumSize;
    }

    void setMinimumSize(int minSize) {
        this.minimumSize = minSize;
    }

    int getSize() {
        return size;
    }

    void setSize(int size) {
        this.size = size;
    }

    void setDisplayCoordinate(int coordinate) {
        getView().setValue(displayRoot, coordinate);
    }

    int getDisplayCoordinate() {
        return getView().getValue(displayRoot);
    }

    abstract AxisView getView();
    abstract Section duplicate(ZoneContext context);
}
