package pl.srw.billcalculator.data;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by Kamil Seweryn.
 */
public class PreviousReadingsAdapter extends ArrayAdapter<String> {

    private static final String[] array = new String[] {"12345", "12432", "1254321", "344634", "12567", "12", "999999999"};
    private Filter mFilter;
    private final Object mLock = new Object();

    public PreviousReadingsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    private void setResult(String[] result) {
        super.clear();
        super.addAll(result);
    }

    private String[] getAll() {
        return array;//TODO temporary dummy collection
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new PreviousReadingFilter();
        }
        return mFilter;
    }
    
    private class PreviousReadingFilter extends Filter {

        @DebugLog
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {//show only last result
                String[] list;
                synchronized (mLock) {
                    final int length = getAll().length;
                    list = Arrays.copyOfRange(getAll(), length-1, length);
                }
                results.values = list;
                results.count = list.length;
            } else {
                String[] values;
                final int count;
                synchronized (mLock) {
                    count = getAll().length;
                    values = Arrays.copyOf(getAll(), count);
                }

                final ArrayList<String> newValues = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    if (values[i].startsWith(prefix.toString())) {
                        newValues.add(values[i]);
                    }
                }

                results.values = newValues.toArray(new String[newValues.size()]);
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            setResult((String[]) results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
