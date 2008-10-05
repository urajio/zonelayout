package com.atticlabs.zonelayout.swing;

import java.awt.Component;
import java.awt.Dimension;

final class DisplaySizeView extends SizeView {
    Dimension getSize(ComponentSizeCache c) {
        return c.getSize();
    }

    int getSize(Section s) {
        return s.getSize();
    }

    void setSize(Section s, int size) {
        s.setSize(size);
    }
}
