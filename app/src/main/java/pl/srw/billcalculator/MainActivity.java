package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.component.CheckPricesDialogFragment;
import pl.srw.billcalculator.form.PgeForm;
import pl.srw.billcalculator.form.fragment.InputFragment;
import pl.srw.billcalculator.form.fragment.LogoFragment;
import pl.srw.billcalculator.preference.GeneralPreferences;
import pl.srw.billcalculator.preference.PgePrices;
import pl.srw.billcalculator.preference.PgnigPrices;

/**
 * Created by Kamil Seweryn.
 */
public class MainActivity extends Activity {

    public static final String TAG_CHECK_PRICES_DIALOG = "CHECK_PRICES_DIALOG";
    private CheckPricesDialogFragment checkPricesDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            replaceFormFragments(PgeForm.getLogoSection(), PgeForm.getInputSection());
        }
    }

    @DebugLog
    @Override
    protected void onResume() {
        super.onResume();
        if (GeneralPreferences.isFirstLaunch()) {
            new PgePrices().setDefault();
            new PgnigPrices().setDefault();
            showCheckPricesDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkPricesDialog != null) {
            checkPricesDialog.dismiss();
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

    public void replaceFormFragments(final LogoFragment logoFragment, final InputFragment inputFragment) {
        getFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fl_logo_section, logoFragment)
                .replace(R.id.fl_input_section, inputFragment)
                .commit();
        // getFragmentManager().executePendingTransactions();
    }

    public void start(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @DebugLog
    private void showCheckPricesDialog() {
        if (checkPricesDialog == null) {
            checkPricesDialog = new CheckPricesDialogFragment();
        }
        checkPricesDialog.show(getFragmentManager(), TAG_CHECK_PRICES_DIALOG);
    }

}
