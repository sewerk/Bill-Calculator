package pl.srw.billcalculator.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import hugo.weaving.DebugLog;

@lombok.ToString
public class MultiSelect<I extends Parcelable> implements Parcelable {

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
        this.selectedItems = in.readSparseArray(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseArray((SparseArray) this.selectedItems);
    }

    @Override
    public int describeContents() {
        return 0;
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
