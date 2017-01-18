package pl.srw.billcalculator.settings.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.f2prateek.dart.InjectExtra;

import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.di.ProviderSettingsComponent;
import pl.srw.billcalculator.settings.fragment.ProviderSettingsFragment;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.wrapper.Dependencies;

/**
 * Created by Kamil Seweryn.
 */
public class ProviderSettingsActivity extends BackableActivity<ProviderSettingsComponent>
        implements ProviderSettingsFragmentOwner {

    private static final String FRAGMENT_TAG = "SettingsFragment";
    private static final String EXTRA_PROVIDER_NAME = "PROVIDER_NAME";
    @InjectExtra(EXTRA_PROVIDER_NAME) Provider provider;

    public static Intent createIntent(final Context context, final Provider type) {
        Intent i = new Intent(context, ProviderSettingsActivity.class);
        i.putExtra(EXTRA_PROVIDER_NAME, type);
        return i;    
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dependencies.inject(this);
        Analytics.logContent(ContentType.SETTINGS, "settings for", provider.toString());

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, ProviderSettingsFragment.newInstance(provider), FRAGMENT_TAG)
                    .commit();
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(provider.titleRes);
    }

    @Override
    public ProviderSettingsFragment getProviderSettingsFragment() {
        return (ProviderSettingsFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public ProviderSettingsComponent prepareComponent() {
        return BillCalculator.get(this).getApplicationComponent().getProviderSettingsComponent();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.settings_provider;
    }
}
