package com.atticlabs.zonelayout.swing;

import java.awt.Component;
import java.awt.Dimension;

abstract class SizeView {
    int getDifference(SizeView view, Section s) {
        return Math.abs(getSize(s) - view.getSize(s)); 
    }

    abstract Dimension getSize(ComponentSizeCache c);
    abstract int getSize(Section s);
    abstract void setSize(Section s, int size);
}
