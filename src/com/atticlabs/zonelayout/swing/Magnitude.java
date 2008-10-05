package com.atticlabs.zonelayout.swing;

public class Magnitude {
    public final int vertical;
    public final int horizontal;

    public Magnitude(Magnitude m) {
        this.vertical = m.vertical;
        this.horizontal = m.horizontal;
    }
    
    public Magnitude(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public String toString() {
        return "(" + horizontal + ", " + vertical + ")";
    }
}
