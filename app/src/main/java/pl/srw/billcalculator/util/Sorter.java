package pl.srw.billcalculator.util;

public final class Sorter {

    /**
     * Revert order of int array
     */
    public static int[] revert(int[] in) {
        if (in.length < 2) {
            return in;
        }
        int[] reverted = new int[in.length];
        int idx = 0;
        for (int i = in.length - 1; i >= 0; i--) {
            reverted[idx++] = in[i];
        }
        return reverted;
    }
}
