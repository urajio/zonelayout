package com.atticlabs.zonelayout.swing;

import java.awt.Component;
import java.awt.Dimension;

final class PreferredSizeView extends SizeView {
    Dimension getSize(ComponentSizeCache c) {
        return c.getPreferredSize();
    }

    int getSize(Section s) {
        return s.getPreferredSize();
    }

    void setSize(Section s, int size) {
        s.setPreferredSize(size);
    }
}
