package pl.srw.billcalculator.util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import hugo.weaving.DebugLog;

@lombok.ToString(includeFieldNames = true)
public class MultiSelect<I extends Parcelable> implements Parcelable {

    private static final String BUNDLE_KEY = "value";
    private SparseArray<I> selectedItems;

    public MultiSelect() {
        selectedItems = new SparseArray<>();
    }

    @DebugLog
    public void select(final int p, final I o) {
        selectedItems.put(p, o);
    }

    @DebugLog
    public void deselect(final int p) {
        selectedItems.remove(p);
    }

    public boolean isSelected(final int p) {
        return selectedItems.indexOfKey(p) >= 0;
    }

    public boolean isAnySelected() {
        return selectedItems.size() > 0;
    }

    public void deselectAll() {
        selectedItems.clear();
    }

    public Collection<I> getItems() {
        final ArrayList<I> values = new ArrayList<>(selectedItems.size());
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

    protected MultiSelect(Parcel in) {
        selectedItems = in.readBundle(getClass().getClassLoader()).getSparseParcelableArray(BUNDLE_KEY);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(BUNDLE_KEY, selectedItems);
        dest.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
        return 1;// TODO: check if 0 is ok
    }

    public static final Creator<MultiSelect> CREATOR = new Creator<MultiSelect>() {
        @Override
        public MultiSelect createFromParcel(Parcel in) {
            return new MultiSelect(in);
        }

        @Override
        public MultiSelect[] newArray(int size) {
            return new MultiSelect[size];
        }
    };
}
