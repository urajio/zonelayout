package com.atticlabs.zonelayout.swing;

final class GiveView extends WeightView {
    double getWeight(Section s) {
        return s.getGive();
    }
}
