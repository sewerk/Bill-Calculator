package pl.srw.billcalculator;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.preference.PricesSettingsFragment;
import pl.srw.billcalculator.preference.SettingsFragment;

/**
 * Created by Kamil Seweryn
 */
public class SettingsActivity extends Activity implements PricesSettingsFragment.PreferenceSubScreenNotifier {

    public static final String FRAGMENT_TAG = "SettingsFragment";
    public static final String RETRIEVE_HELP_DIALOG = "helpDialogIsShowing";
    private MenuItem helpMenuItem;
    private Dialog helpDialog;

    private boolean retrieveHelpDialog;//TODO: handle by hrisay

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment(), FRAGMENT_TAG)
                    .commit();
        }

        initHelpDialog();
    }

    private void initHelpDialog() {
        helpDialog = new Dialog(this);
        helpDialog.setTitle(R.string.action_help);
    }

    @DebugLog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        helpMenuItem = menu.getItem(0);
        if (isMainSettingsScreen())
            helpMenuItem.setVisible(false);
        return true;
    }

    private boolean isMainSettingsScreen() {
        return getFragmentManager().findFragmentByTag(SettingsActivity.FRAGMENT_TAG) instanceof SettingsFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            helpDialog.show();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RETRIEVE_HELP_DIALOG, retrieveHelpDialog);
    }

    @DebugLog
    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        retrieveHelpDialog = savedInstanceState.getBoolean(RETRIEVE_HELP_DIALOG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (retrieveHelpDialog) {
            helpDialog.show();
            retrieveHelpDialog = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (helpDialog.isShowing()) {
            helpDialog.dismiss();
            retrieveHelpDialog = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helpMenuItem.setVisible(false);
        getActionBar().setTitle(R.string.settings_label);
    }

    @Override
    public void setHelpResource(final int layout) {
        helpMenuItem.setVisible(true);
        helpDialog.setContentView(layout);
        helpDialog.findViewById(R.id.b_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                helpDialog.dismiss();
            }
        });
    }
}
