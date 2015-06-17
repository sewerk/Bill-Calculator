package pl.srw.billcalculator.settings.activity;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 14.04.15.
 */
public class SettingsPresenter implements ISettingsPresenter {

    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    public static final String[] COLUMNS = {TITLE, DESCRIPTION};

    private final ISettingsView view;
    private List<Map<String, String>> entries = new ArrayList<>();

    public SettingsPresenter(ISettingsView view) {
        this.view = view;
        if (entries.isEmpty()) {
            addEntries(Provider.values());
            entries = Collections.unmodifiableList(entries);
        }
        view.fillList(entries, COLUMNS);
    }

    @Override
    public Provider getProviderAt(final int position) {
        return Provider.values()[position];
    }

    private void addEntries(Provider[] values) {
        for (Provider provider : values) {
            addEntry(getString(provider.titleRes), getString(provider.settingsDescRes));
        }
    }

    private void addEntry(String title, String desc) {
        HashMap<String, String> entry = new HashMap<>(2);
        entry.put(TITLE, title);
        entry.put(DESCRIPTION, desc);
        entries.add(Collections.unmodifiableMap(entry));
    }

    private String getString(@StringRes final int strRes) {
        return view.getString(strRes);
    }
}
