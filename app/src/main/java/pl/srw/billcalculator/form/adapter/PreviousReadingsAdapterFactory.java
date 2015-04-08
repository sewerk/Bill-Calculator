package pl.srw.billcalculator.form.adapter;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import pl.srw.billcalculator.persistence.type.CurrentReadingType;

/**
 * Created by Kamil Seweryn.
 */
public final class PreviousReadingsAdapterFactory {

    private static final Map<CurrentReadingType, PreviousReadingsAdapter> adapters = new HashMap<>(4);

    public static PreviousReadingsAdapter build(final Context context, final CurrentReadingType readingType) {
        if (!adapters.containsKey(readingType))
            adapters.put(readingType, new PreviousReadingsAdapter(context, readingType));
        return adapters.get(readingType);
    }
}
