package com.atticlabs.zonelayout.swing;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Random;

public class SpeedTest extends TestCase {
    public SpeedTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SpeedTest.class);
    }

    public void testRedistributeValues() {
        Random r = new Random();
        r.nextInt();
        int[] values = new int[50];
        double[] dValues = new double[50];

        long startTime = System.currentTimeMillis();
        for (int j = 0; j < 10000; j++) {
            int total = 0;
            for (int i = 0; i < values.length; i++) {
                values[i] = r.nextInt(100);
                total += values[i];
            }
            int newTotal = total + r.nextInt(4500) + 500;
            MathRoutines.proportionallyRedistributeValues(values, total, newTotal);
        }
        System.out.println("int time:" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int j = 0; j < 10000; j++) {
            int total = 0;
            for (int i = 0; i < dValues.length; i++) {
                dValues[i] = r.nextInt(100);
                total += dValues[i];
            }
            int newTotal = total + r.nextInt(4500) + 500;
            MathRoutines.proportionallyRedistributeValues(dValues, total, newTotal);
        }
        System.out.println("double time:" + (System.currentTimeMillis() - startTime));

    }
}

