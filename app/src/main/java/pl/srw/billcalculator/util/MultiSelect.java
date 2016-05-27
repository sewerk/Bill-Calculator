package pl.srw.billcalculator.util;

import android.support.v4.util.ArrayMap;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hugo.weaving.DebugLog;

@lombok.ToString(includeFieldNames = true)
public class MultiSelect<P, I> {

    private Map<P, I> selectedItems;

    public MultiSelect() {
        selectedItems = new ArrayMap<>();
    }

    @DebugLog
    public void select(final P p, final I o) {
        selectedItems.put(p, o);
    }

    @DebugLog
    public void deselect(final P p) {
        selectedItems.remove(p);
    }

    public boolean isSelected(final P p) {
        return selectedItems.containsKey(p);
    }

    public boolean isAnySelected() {
        return selectedItems.size() > 0;
    }

    public void deselectAll() {
        selectedItems.clear();
    }

    public Collection<I> getItems() {
        return selectedItems.values();
    }

    public Collection<P> getPositionsReverseOrder() {
        final List<P> keys = new LinkedList<>(selectedItems.keySet());
        Collections.sort(keys, Collections.reverseOrder());
        return keys;
    }
}
