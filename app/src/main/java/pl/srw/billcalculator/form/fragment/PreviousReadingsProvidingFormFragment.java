package pl.srw.billcalculator.form.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.event.HistoryChangedEvent;
import pl.srw.billcalculator.event.PreviousReadingsNeedsUpdateEvent;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 15.06.15.
 */
public abstract class PreviousReadingsProvidingFormFragment extends Fragment {

    private Lock lock;
    private SimpleArrayMap<CurrentReadingType, int[]> prevReadingsCache;//TODO: improve: move cache to presenter and prevent from repopulate on rotation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lock = new ReentrantLock();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @DebugLog
    public void onEventBackgroundThread(HistoryChangedEvent event) {
        if (event.getForProvider() == getProvider())
            cachePreviousReadings();
    }

    public void onEventMainThread(PreviousReadingsNeedsUpdateEvent event) {
        if (event.getForProvider() == getProvider())
            setPreviousReadings();
    }

    protected abstract Provider getProvider();

    protected abstract CurrentReadingType[] getCurrentReadingTypes();

    protected abstract void setPreviousReadings();

    protected void cachePreviousReadings() {
        prevReadingsCache = new SimpleArrayMap<>();
        lock.lock();
        try {
            fillCacheFor(getCurrentReadingTypes());
        } finally {
            lock.unlock();
            EventBus.getDefault().post(new PreviousReadingsNeedsUpdateEvent(getProvider()));
        }
    }

    protected int[] getPreviousReadings(CurrentReadingType readingType) {
        lock.lock();
        try {
            return prevReadingsCache.get(readingType);
        } finally {
            lock.unlock();
        }
    }

    @DebugLog
    private void fillCacheFor(CurrentReadingType...types) {
        for (CurrentReadingType type : types)
            prevReadingsCache.put(type, Database.queryCurrentReadings(type));
    }
}
