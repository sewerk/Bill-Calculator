package pl.srw.billcalculator.form.autocomplete;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;

import pl.srw.billcalculator.R;

public class PreviousReadingsAdapter extends ArrayAdapter<String> {

    private Filter mFilter;
    private final String[] allData;

    public PreviousReadingsAdapter(Context context, int[] readings) {
        super(context, R.layout.form_autocomplete_item, R.id.autocomplete_text);
        allData = toStringArray(readings);
    }

    private String[] toStringArray(int[] readings) {
        if (readings == null || readings.length == 0)
            return new String[0];

        String[] result = new String[readings.length];
        for (int i = 0; i < readings.length; i++) {
            result[i] = String.valueOf(readings[i]);
        }
        return result;
    }

    private void setResult(String[] result) {
        super.clear();
        super.addAll(result);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new PreviousReadingFilter();
        }
        return mFilter;
    }

    @VisibleForTesting
    static String[] filterData(CharSequence prefix, String[] allData) {
        if (allData.length == 0) {
            return allData;
        } else if (prefix == null || prefix.length() == 0) {//show only last result
            return new String[] { allData[0]};
        } else {
            final ArrayList<String> newValues = new ArrayList<>(allData.length);
            for (String value : allData) {
                if (value.startsWith(prefix.toString())) {
                    newValues.add(value);
                }
            }
            return newValues.toArray(new String[newValues.size()]);
        }
    }

    private class PreviousReadingFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            String[] newValues = filterData(prefix, allData);

            FilterResults results = new FilterResults();
            results.values = newValues;
            results.count = newValues.length;
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