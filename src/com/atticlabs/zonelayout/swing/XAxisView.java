package com.atticlabs.zonelayout.swing;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.util.List;

final class XAxisView extends AxisView {

    int getValue(Point p) {
        return p.x;
    }

    void setValue(Point p, int value) {
        p.x = value;
    }

    int getSize(Dimension d) {
        return d.width;
    }

    int getSize(Zone zone) {
        return zone.getWidth();
    }

    int getTake(Zone zone) {
        return zone.getTake().horizontal;
    }

    int getGive(Zone zone) {
        return zone.getGive().horizontal;
    }

    List getSections(ZoneLayoutImpl layout) {
        return layout.columns;
    }

    int getAdjustment(Insets insets) {
        return insets.left;
    }

    int getValue(Magnitude magnitude) {
        return magnitude.horizontal;
    }
}
