package com.atticlabs.zonelayout.swing;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.util.List;

final class YAxisView extends AxisView {

    int getValue(Point p) {
        return p.y;
    }

    void setValue(Point p, int value) {
        p.y = value;
    }


    int getSize(Dimension d) {
        return d.height;
    }

    int getSize(Zone zone) {
        return zone.getHeight();
    }

    int getTake(Zone zone) {
        return zone.getTake().vertical;
    }

    int getGive(Zone zone) {
        return zone.getGive().vertical;
    }

    List getSections(ZoneLayoutImpl layout) {
        return layout.rows;
    }

    int getAdjustment(Insets insets) {
        return insets.top;
    }

    int getValue(Magnitude magnitude) {
        return magnitude.vertical;
    }
}
