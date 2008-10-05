package com.atticlabs.zonelayout.swing;

import java.awt.Component;
import java.awt.Dimension;

public final class Preset {
    static final String SPECIAL_CHARACTERS = "~!*";

    final static Magnitude DEFAULT_TAKE = new Magnitude(0, 0);
    final static Dimension DIMENSION_ZERO = new Dimension(0, 0);
    final static Preset HORIZONTAL_EXPANDER = createExpander(1, 0);
    final static Preset VERTICAL_EXPANDER = createExpander(0, 1);
    final static Preset BOTH_EXPANDER = createExpander(1, 1);
    final Dimension min;
    final Dimension pref;
    final Magnitude take;

    private Preset(Dimension min, Dimension pref, Magnitude take) {
        this.min = min;
        this.pref = pref;
        this.take = take;
    }

    static Preset getSpecialPreset(char c) {
        switch (c) {
            case '~':
                return HORIZONTAL_EXPANDER;
            case '!':
                return VERTICAL_EXPANDER;
            case '*':
                return BOTH_EXPANDER;
            default:
                throw new RuntimeException("Unknown special character '" + c + "'");
        }
    }

    public static Preset createSpacer(int width, int height) {
        return new Preset(new Dimension(width, height), new Dimension(width, height), DEFAULT_TAKE);
    }

    public static Preset createExpander(int takeX, int takeY) {
        return new Preset(DIMENSION_ZERO, DIMENSION_ZERO, new Magnitude(takeX, takeY));
    }

    Component createComponent() {
        return new Component() {
            public Dimension getPreferredSize() {
                return pref;
            }

            public Dimension getMinimumSize() {
                return min;
            }
        };
    }

    void setUpZone(Zone z) {
        z.preset  = this;
        z.setComponent(createComponent());
        z.take = this.take;
//        z.give = Zone.MAX_GIVE_OR_TAKE;
    }
}
