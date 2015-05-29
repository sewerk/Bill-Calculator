package pl.srw.billcalculator.form.adapter;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;

/**
 * Created by Kamil Seweryn.
 */
public final class PreviousReadingsAdapterFactory {

    private static final Map<CurrentReadingType, PreviousReadingsAdapter> adapters = new HashMap<>(4);

    public static PreviousReadingsAdapter build(final Context context, final CurrentReadingType readingType) {
        final PreviousReadingsAdapter adapter = findOrCreate(context, readingType);
        registerToEventBus(adapter);
        return adapter;
    }

    private static PreviousReadingsAdapter findOrCreate(Context context, CurrentReadingType readingType) {
        if (!adapters.containsKey(readingType))
            adapters.put(readingType, new PreviousReadingsAdapter(context, readingType));
        return adapters.get(readingType);
    }

    private static void registerToEventBus(PreviousReadingsAdapter adapter) {
        final EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(adapter))
            eventBus.register(adapter);
    }

    @DebugLog
    public static void onDestroy() {
        final EventBus eventBus = EventBus.getDefault();
        for (PreviousReadingsAdapter adapter : adapters.values()) {
            if (eventBus.isRegistered(adapter))
                eventBus.unregister(adapter);
        }
        adapters.clear();
    }
}
