package pl.srw.billcalculator.util;

import java.util.Collection;

/**
 * Created by Kamil Seweryn.
 */
final public class Strings {

    private Strings() {}

    public static String[] toArray(Collection collection) {
        String[] result = new String[collection.size()];
        int i = 0;
        for (Object o : collection)
            result[i++] = o.toString();
        return result;
    }

    public static String[] toArray(int[] array) {
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = Integer.toString(array[i]);
        return result;
    }
}
