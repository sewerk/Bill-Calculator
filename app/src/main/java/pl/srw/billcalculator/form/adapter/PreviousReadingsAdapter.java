package pl.srw.billcalculator.form.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Arrays;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.util.Strings;

/**
 * Created by Kamil Seweryn.
 */
@lombok.ToString(of = {"prevReadings" })
public class PreviousReadingsAdapter extends ArrayAdapter<String> {

    private Filter mFilter;
    private final Object mLock = new Object();
    private final String[] prevReadings;

    public PreviousReadingsAdapter(Context context, int[] readings) {
        super(context, R.layout.dropdowntext, R.id.dropDown);
        prevReadings = Strings.toArray(readings);
    }

    private void setResult(String[] result) {
        super.clear();
        super.addAll(result);
    }

    private String[] getAll() {
        String[] allReadings;
        synchronized (mLock) {
            allReadings = prevReadings;
        }

        if (allReadings == null)
            return new String[0];
        else
            return allReadings;
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

            String[] all = getAll();
            final int length = all.length;

            if (length == 0) {
                results.values = all;
                results.count = 0;
            } else if (prefix == null || prefix.length() == 0) {//show only last result
                results.values = getLastReading(all);
                results.count = 1;
            } else {
                String[] values = Arrays.copyOf(all, length);

                final ArrayList<String> newValues = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    if (values[i].startsWith(prefix.toString())) {
                        newValues.add(values[i]);
                    }
                }

                results.values = newValues.toArray(new String[newValues.size()]);
                results.count = newValues.size();
            }

            return results;
        }

        private String[] getLastReading(final String[] all) {
            return Arrays.copyOfRange(all, 0, 1);
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
