package pl.srw.billcalculator.settings.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuItem;

import com.f2prateek.dart.InjectExtra;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.Provider;
import pl.srw.billcalculator.settings.fragment.PgeSettingsFragment;
import pl.srw.billcalculator.settings.fragment.PgnigSettingsFragment;
import pl.srw.billcalculator.settings.fragment.ProviderSettingsFragment;
import pl.srw.billcalculator.settings.fragment.TauronSettingsFragment;

/**
 * Created by Kamil Seweryn.
 */
public class ProviderSettingsActivity extends BackableActivity {

    private static final String FRAGMENT_TAG = "SettingsFragment";
    private static final String EXTRA_PROVIDER_NAME = "PROVIDER_NAME";
    @InjectExtra(EXTRA_PROVIDER_NAME) String providerName;

    public static Intent createIntent(final Context context, final Provider type) {
        Intent i = new Intent(context, ProviderSettingsActivity.class);
        i.putExtra(EXTRA_PROVIDER_NAME, type.toString());
        return i;    
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProviderSettingsFragment preferenceFragment;
        if (savedInstanceState == null) {
            preferenceFragment = getProviderSettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, preferenceFragment, FRAGMENT_TAG)
                    .commit();

        } else {
            preferenceFragment = getCurrentFragment();
        }

        if (getActionBar() != null && preferenceFragment != null)
            getActionBar().setTitle(preferenceFragment.getTitleResource());
    }

    private ProviderSettingsFragment getProviderSettingsFragment() {
        final Provider provider = Provider.valueOf(providerName);
        switch (provider) {
            case PGNIG:
                return new PgnigSettingsFragment();
            case PGE:
                return new PgeSettingsFragment();
            case TAURON:
                return new TauronSettingsFragment();
        }
        throw new RuntimeException("No settings screen for " + provider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelp();
            return true;
        } else if (item.getItemId() == R.id.action_default) {
            getCurrentFragment().restoreDefault();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelp() {
        final Intent intent = ProviderSettingsHelpActivity.createIntent(this, getHelpResource(), getExampleImageResource());
        startActivity(intent);
    }

    private @LayoutRes int getHelpResource() {
        return getCurrentFragment().getHelpLayoutResource();
    }

    private @DrawableRes int getExampleImageResource() {
        return getCurrentFragment().getHelpImageExampleResource();
    }

    private ProviderSettingsFragment getCurrentFragment() {
        return (ProviderSettingsFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }
}
