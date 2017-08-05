package pl.srw.billcalculator.settings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.util.ArrayMap;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.SettingsPresenter;
import pl.srw.billcalculator.settings.di.SettingsComponent;
import pl.srw.billcalculator.settings.fragment.ProviderSettingsFragment;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.wrapper.Dependencies;
import pl.srw.mfvp.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.presenter.PresenterOwner;
import pl.srw.mfvp.presenter.SinglePresenterHandlingDelegate;

public class SettingsActivity extends BackableActivity<SettingsComponent>
        implements PresenterOwner, SettingsPresenter.SettingsView,
        ProviderSettingsFragmentOwner {

    private static final String FRAGMENT_TAG = "SettingsFragment";
    private static final String ICON = "ICON";
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String[] COLUMNS = {ICON, TITLE, DESCRIPTION};

    @BindView(R.id.list) ListView list;
    @Nullable @BindView(R.id.prefs_frame) FrameLayout frameView;

    @Inject SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        presenter.setup(isDualPane());
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.settings;
    }

    @Override
    public SettingsComponent prepareComponent() {
        return Dependencies.getApplicationComponent().getSettingsComponent();
    }

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }

    @Override
    public ProviderSettingsFragment getProviderSettingsFragment() {
        return (ProviderSettingsFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public void selectProvider(final int position) {
        list.setItemChecked(position, true);
    }

    @Override
    public void showSettingsScreenFor(Provider provider) {
        final Intent intent = ProviderSettingsActivity.createIntent(SettingsActivity.this, provider);
        startActivity(intent);
    }

    @Override
    public void showSettingsFor(Provider provider) {
        final ProviderSettingsFragment fragment = ProviderSettingsFragment.newInstance(provider);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.prefs_frame, fragment, FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void fillProviderList(Provider[] providers) {
        list.setAdapter(prepareAdapter(providers));
    }

    @OnItemClick(R.id.list)
    void onProviderClicked(int position) {
        presenter.providerClicked(position);
    }

    private ListAdapter prepareAdapter(Provider[] providers) {
        return new SimpleAdapter(this, getProviderEntries(providers), R.layout.settings_list_item,
                COLUMNS, new int[]{R.id.icon, R.id.title, R.id.summary});
    }

    private boolean isDualPane() {
        return frameView != null;
    }

    private List<Map<String, Object>> getProviderEntries(Provider[] providers) {
        List<Map<String, Object>> entries = new ArrayList<>();
        for (Provider provider : providers) {
            final ArrayMap<String, Object> entry = addEntry(provider.logoSmallRes, provider.settingsTitleRes, provider.settingsDescRes);
            entries.add(entry);
        }
        return entries;
    }

    private ArrayMap<String, Object> addEntry(@DrawableRes int icon, @StringRes final int title, @StringRes int desc) {
        ArrayMap<String, Object> entry = new ArrayMap<>(3);
        entry.put(ICON, icon);
        entry.put(TITLE, getString(title));
        entry.put(DESCRIPTION, getString(desc));
        return entry;
    }
}
