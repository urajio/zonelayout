package com.atticlabs.zonelayout.swing;

final class TakeView extends WeightView {
    double getWeight(Section s) {
        return s.getTake(); 
    }
}
