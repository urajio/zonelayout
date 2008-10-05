package com.atticlabs.zonelayout.swing;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.util.List;

abstract class AxisView {
    static final AxisView Y = new YAxisView();
    static final AxisView X = new XAxisView();

    int getStart(Zone zone) {
        return getValue(zone.getRoot());
    }

    int getFinish(Zone zone) {
        return getValue(zone.getSecondRoot());
    }

    void adjust(Point p, int amount) {
        setValue(p, getValue(p) + amount);
    }

    boolean contains(Zone zone, int value) {
        return (value >= getStart(zone)  && value <= getFinish(zone));
    }

    abstract int getAdjustment(Insets insets);
    abstract int getValue(Point p);
    abstract void setValue(Point p, int value);
    abstract int getSize(Dimension d);
    abstract int getSize(Zone zone);
    abstract int getTake(Zone zone);
    abstract int getGive(Zone zone);
    abstract List getSections(ZoneLayoutImpl layout);
    abstract int getValue(Magnitude magnitude);

}
