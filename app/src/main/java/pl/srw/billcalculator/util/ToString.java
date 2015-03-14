package pl.srw.billcalculator.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import hugo.weaving.DebugLog;

/**
 * Created by Kamil Seweryn.
 */
final public class ToString {

    private ToString() {}

    @DebugLog
    public static String[] toArray(Collection collection) {
        List<String> res = new ArrayList<>(collection.size());
        for (Object o : collection) {
            res.add(o.toString());
        }
        return res.toArray(new String[res.size()]);
    }
}
