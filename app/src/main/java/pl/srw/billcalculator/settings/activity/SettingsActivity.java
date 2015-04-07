package pl.srw.billcalculator.settings.activity;

import android.os.Bundle;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.settings.fragment.SettingsFragment;


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
