package pl.srw.billcalculator.util;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.db.Bill;

@Singleton
public class BillSelection {

    private SparseArray<Bill> selectedItems;

    @Inject
    public BillSelection() {
        selectedItems = new SparseArray<>();
    }

    @DebugLog
    public void select(final int position, final Bill o) {
        selectedItems.put(position, o);
    }

    @DebugLog
    public void deselect(final int position) {
        selectedItems.remove(position);
    }

    public boolean isSelected(final int position) {
        return selectedItems.indexOfKey(position) >= 0;
    }

    public boolean isAnySelected() {
        return selectedItems.size() > 0;
    }

    public void deselectAll() {
        selectedItems.clear();
    }

    public Collection<Bill> getItems() {
        final ArrayList<Bill> values = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            values.add(selectedItems.valueAt(i));
        }
        return values;
    }

    public int[] getPositionsReverseOrder() {
        int[] keys = new int[selectedItems.size()];
        for (int i = 0; i < selectedItems.size(); i++) {
            keys[i] = selectedItems.keyAt(i);
        }
        Arrays.sort(keys);
        return Sorter.revert(keys);
    }
}
