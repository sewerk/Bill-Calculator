package pl.srw.billcalculator.settings.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.srw.billcalculator.settings.Provider;

/**
 * Created by kseweryn on 14.04.15.
 */
public class SettingsPresenter {

    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String[] COLUMNS = {TITLE, DESCRIPTION};
    private List<Map<String, String>> entries = new ArrayList<>();

    private Context context;

    public SettingsPresenter(final Context context) {
        this.context = context;
        addEntries(Provider.values());
        entries = Collections.unmodifiableList(entries);
        Toast.makeText(context, "PRESENTER created", Toast.LENGTH_SHORT).show();//TODO remove
    }

    public List<Map<String, String>> getEntries() {
        return entries;
    }

    public String[] getFromColumns() {
        return COLUMNS;
    }

    public void itemClicked(final int position) {
        final Intent intent = ProviderSettingsActivity.createIntent(context, getProviderFor(position));
        context.startActivity(intent);
    }

    private Provider getProviderFor(final int position) {
        return Provider.values()[position];
    }

    private void addEntries(Provider[] values) {
        for (Provider provider : values) {
            addEntry(getString(provider.titleRes), getString(provider.descRes));
        }
    }

    private void addEntry(String title, String desc) {
        HashMap<String, String> entry = new HashMap<>(2);
        entry.put(TITLE, title);
        entry.put(DESCRIPTION, desc);
        entries.add(Collections.unmodifiableMap(entry));
    }

    private String getString(@StringRes final int strRes) {
        return context.getString(strRes);
    }

}
