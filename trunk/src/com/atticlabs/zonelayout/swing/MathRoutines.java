package com.atticlabs.zonelayout.swing;

import java.util.Arrays;

public class MathRoutines {
    public static void proportionallyRedistributeValues(int[] values, int oldTotal, int newTotal) {
        int[] remainders = new int[values.length];
        int[] valueIndices = new int[values.length];
        int remainingValue = newTotal;

        for (int i = 0; i < values.length; i++) {
            int newValue = (int) (((long) values[i]) * ((long)newTotal) / ((long) oldTotal));
            int remainder = (int) (((long) values[i]) * ((long)newTotal) % ((long) oldTotal));

            int index = 0;
            for (; index < i; index++) {
                if (remainder > remainders[index]) {
                    System.arraycopy(remainders, index, remainders, index + 1, i - index);
                    System.arraycopy(valueIndices, index, valueIndices, index + 1, i - index);
                    break;
                }
            }
            remainders[index] = remainder;
            valueIndices[index] = i;

            values[i] = newValue;
            remainingValue -= newValue;
        }

        for (int i = 0; remainingValue > 0; remainingValue--, i++) {
            values[valueIndices[i]]++;
        }
    }

    public static void proportionallyRedistributeValues(double[] values, double oldTotal, double newTotal) {
        double factor = newTotal / oldTotal;
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i] * factor;
        }
    }


    public static int sum(int[] nums) {
        int sum = 0;
        for (int j = 0; j < nums.length; j++) {
            sum += nums[j];
        }
        return sum;
    }

    public static double sum(double[] nums) {
        double sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        return sum;
    }

    public static int[] getSpaceToTake(double[] takes, int space) {
        double totalTake = sum(takes);
        int[] results = new int[takes.length];
        double[] toTake = new double[takes.length];

        if (totalTake == 0) {
            Arrays.fill(results, 0);
            return results;
        }

        double carryOver = 0.0;
        for (int i = 0; i < takes.length; i++) {
            toTake[i] = space / totalTake * takes[i];
            double oldValue = toTake[i];
            toTake[i] = Math.round(toTake[i] + carryOver);
            carryOver += oldValue - toTake[i];
            results[i] = (int) toTake[i];
        }

        return results;
    }

    public static int[] getSpaceToGive(double[] gives, int[] spaceAvailable, int space) {
        int giveableSpace = sum(spaceAvailable);

        int[] results = new int[gives.length];

        if (space > giveableSpace) {
            for (int i = 0; i < gives.length; i++) {
                if (gives[i] > 0) {
                    results[i] = spaceAvailable[i];
                }
            }
            return results;
        }

        if (giveableSpace == 0) {
            Arrays.fill(results, 0);
            return results;
        }

        double totalGive = sum(gives);
        double[] toGive = new double[gives.length];
        double[] dSpaceAvailable = new double[spaceAvailable.length];
        for (int i = 0; i < spaceAvailable.length; i++) {
            dSpaceAvailable[i] = spaceAvailable[i];
        }
        Arrays.fill(toGive, 0);
        double remainingSpace = space;

        while (remainingSpace > 0.0000001 && totalGive > 0) {
            double share = remainingSpace / totalGive;

            for (int i = 0; i < gives.length; i++) {
                double newGiveAmount = Math.min(share * gives[i], dSpaceAvailable[i]);
                toGive[i] += newGiveAmount;
                remainingSpace -= newGiveAmount;
                dSpaceAvailable[i] -= newGiveAmount;
                if (dSpaceAvailable[i] < 0.0000001) {
                    totalGive -= gives[i];
                }
            }
        }

        double carryOver = 0.0;
        for (int i = 0; i < toGive.length; i++) {
            double oldValue = toGive[i];
            toGive[i] = Math.round(toGive[i] + carryOver);
            carryOver += oldValue - toGive[i];
            results[i] = (int) toGive[i];
        }

        return results;
    }

    public static String toString(int[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(i != 0 ? " " : "");
            sb.append(array[i]);
        }
        return sb.toString();
    }

    public static String toString(double[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(i != 0 ? " " : "");
            sb.append(array[i]);
        }
        return sb.toString();
    }
}
