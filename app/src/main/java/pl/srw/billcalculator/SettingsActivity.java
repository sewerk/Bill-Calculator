package pl.srw.billcalculator;

import android.os.Bundle;

import pl.srw.billcalculator.preference.SettingsFragment;


/**
 * Created by Kamil Seweryn
 */
public class SettingsActivity extends BackableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }
}
