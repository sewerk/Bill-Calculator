package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pl.srw.billcalculator.component.CheckPricesDialogFragment;
import pl.srw.billcalculator.form.ProviderForm;
import pl.srw.billcalculator.preference.GeneralPreferences;

/**
 * Created by Kamil Seweryn.
 */
public class MainActivity extends Activity {

    public static final String TAG_CHECK_PRICES_DIALOG = "CHECK_PRICES_DIALOG";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            replaceFormFragments(ProviderForm.PGE);
            if (GeneralPreferences.isFirstLaunch())
                new CheckPricesDialogFragment()
                        .show(getFragmentManager(), TAG_CHECK_PRICES_DIALOG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            start(SettingsActivity.class);
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            start(AboutActivity.class);
            return true;
        } else if (item.getItemId() == R.id.action_history) {
            start(HistoryActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFormFragments(final ProviderForm form) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.come_closer, 0)
                .replace(R.id.fl_logo_section, form.getLogoFragment())
                .replace(R.id.fl_input_section, form.getInputFragment())
                .commit();
        // getFragmentManager().executePendingTransactions();
    }

    public void start(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void replaceFormFragments(boolean isEnergyForm) {
        if (isEnergyForm)
            replaceFormFragments(ProviderForm.PGNIG);
        else
            replaceFormFragments(ProviderForm.PGE);
    }
}
