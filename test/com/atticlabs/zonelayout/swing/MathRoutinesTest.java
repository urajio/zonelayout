package com.atticlabs.zonelayout.swing;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Arrays;

public class MathRoutinesTest extends TestCase {
    public MathRoutinesTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(MathRoutinesTest.class);
    }

    public void testGetSpaceToTake() throws Exception {
        double[] takes = null;

        takes = new double[] { 10, 10, 10 };
        assertTrue(Arrays.equals(new int[] { 10, 10, 10}, MathRoutines.getSpaceToTake(takes, 30)));

        takes = new double[] { 10, 0, 10 };
        assertTrue(Arrays.equals(new int[] { 15, 0, 15}, MathRoutines.getSpaceToTake(takes, 30)));

        takes = new double[] { 10, 0, 10 };
        assertTrue(Arrays.equals(new int[] { 16, 0, 15}, MathRoutines.getSpaceToTake(takes, 31)));
    }

    public void testGetSpaceToGive() throws Exception {
        double[] gives = null;
        int[] available = null;

        gives = new double[] { 10, 10, 10 };
        available = new int[] { 50, 5, 50 };
        assertTrue(Arrays.equals(new int[] { 13, 5, 12}, MathRoutines.getSpaceToGive(gives, available, 30)));

        gives = new double[] { 10, 10, 10 };
        available = new int[] { 50, 0, 0 };
        assertTrue(Arrays.equals(new int[] { 30, 0, 0}, MathRoutines.getSpaceToGive(gives, available, 30)));

        gives = new double[] { 10, 10, 10 };
        available = new int[] { 50, 0, 1 };
        assertTrue(Arrays.equals(new int[] { 29, 0, 1}, MathRoutines.getSpaceToGive(gives, available, 30)));

        gives = new double[] { 10, 10, 10 };
        available = new int[] { 50, 1, 1 };
        assertTrue(Arrays.equals(new int[] { 28, 1, 1}, MathRoutines.getSpaceToGive(gives, available, 30)));

        gives = new double[] { 10, 1, 10 };
        available = new int[] { 50, 100, 1 };
        assertTrue(Arrays.equals(new int[] { 26, 3, 1}, MathRoutines.getSpaceToGive(gives, available, 30)));

        gives = new double[] { 10, 1, 10 };
        available = new int[] { 50, 100, 1 };
        assertTrue(Arrays.equals(new int[] { 22, 2, 1}, MathRoutines.getSpaceToGive(gives, available, 25)));

        gives = new double[] { 10, 1, 10 };
        available = new int[] { 50, 100, 1 };
        assertTrue(Arrays.equals(new int[] { 20, 2, 1}, MathRoutines.getSpaceToGive(gives, available, 23)));

        gives = new double[] { 10, 1, 10 };
        available = new int[] { 5, 5, 5 };
        assertTrue(Arrays.equals(new int[] { 5, 5, 5}, MathRoutines.getSpaceToGive(gives, available, 25)));

        gives = new double[] { 10, 0, 10 };
        available = new int[] { 5, 5, 5 };
        assertTrue(Arrays.equals(new int[] { 5, 0, 5}, MathRoutines.getSpaceToGive(gives, available, 25)));

        gives = new double[] { 0, 1 };
        available = new int[] { 10, 10 };
        assertTrue(Arrays.equals(new int[] { 0, 10 }, MathRoutines.getSpaceToGive(gives, available, 11)));
    }
}
