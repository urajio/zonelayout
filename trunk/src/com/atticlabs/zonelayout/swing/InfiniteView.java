package com.atticlabs.zonelayout.swing;

import java.awt.Component;
import java.awt.Dimension;

final class InfiniteView extends SizeView {
    Dimension getSize(ComponentSizeCache c) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    int getSize(Section s) {
        return Integer.MAX_VALUE;
    }

    void setSize(Section s, int size) {
        // noop
    }

}
