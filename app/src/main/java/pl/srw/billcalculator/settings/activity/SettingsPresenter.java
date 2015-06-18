package pl.srw.billcalculator.settings.activity;

import android.support.annotation.DrawableRes;
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
public class SettingsPresenter implements SettingsPresenting {

    private static final String ICON = "ICON";
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    public static final String[] COLUMNS = {ICON, TITLE, DESCRIPTION};

    private final SettingsViewing view;
    private List<Map<String, Object>> entries = new ArrayList<>();

    public SettingsPresenter(SettingsViewing view) {
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
            addEntry(provider.logoSmallRes, provider.titleRes, provider.settingsDescRes);
        }
    }

    private void addEntry(@DrawableRes int icon, @StringRes final int title, @StringRes int desc) {
        HashMap<String, Object> entry = new HashMap<>(2);
        entry.put(ICON, icon);
        entry.put(TITLE, getString(title));
        entry.put(DESCRIPTION, getString(desc));
        entries.add(Collections.unmodifiableMap(entry));
    }

    private String getString(@StringRes final int strRes) {
        return view.getString(strRes);
    }
}
