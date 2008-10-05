package com.atticlabs.zonelayout.swing;

import java.awt.Dimension;
import java.awt.Component;

public class ComponentSizeCache {
    public static final Dimension DEFAULT_MINIMUM = new Dimension(0, 0);
    public static final Dimension DEFAULT_PREFERRED = new Dimension(0, 0);
    public static final Dimension DEFAULT_MAXIMUM = new Dimension(0, 0);

    Dimension minimum = DEFAULT_MINIMUM;
    boolean minimumSet;
    Dimension preferred = DEFAULT_PREFERRED;
    boolean preferredSet;
    Dimension maximum = DEFAULT_MAXIMUM;
    boolean maximumSet;
    Component component;

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Dimension getMinimumSize() {
        if (! minimumSet && component != null) {
            this.minimum = component.getMinimumSize();
            Dimension pref = getPreferredSize();
            if (minimum.width > pref.width || minimum.height > pref.height) {
                this.minimum = new Dimension(
                        Math.min(minimum.width, pref.width),
                        Math.min(minimum.height, pref.height)
                );
            }
            minimumSet = true;
        }
        return minimum;
    }

    public Dimension getPreferredSize() {
        if (! preferredSet&& component != null) {
            this.preferred = component.getPreferredSize();
            preferredSet = true;
        }
        return preferred;
    }

    public Dimension getMaximumSize() {
        if (! maximumSet && component != null) {
            this.maximum = component.getMaximumSize();
            maximumSet = true;
        }
        return maximum;
    }

    public Dimension getSize() {
        return component != null ? component.getSize() : DEFAULT_MINIMUM;
    }

    public void clearCache() {
        minimum = null;
        preferred = null;
        maximum = null;
        minimumSet = false;
        preferredSet = false;
        maximumSet = false;
    }
}
