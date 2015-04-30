package pl.srw.billcalculator.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nucleus.view.NucleusActivity;
import nucleus.view.RequiresPresenter;
import pl.srw.billcalculator.form.view.SlidingTabLayout;

import pl.srw.billcalculator.AboutActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.activity.SettingsActivity;
import pl.srw.billcalculator.form.component.CheckPricesDialogFragment;
import pl.srw.billcalculator.history.HistoryActivity;
import pl.srw.billcalculator.settings.GeneralPreferences;

/**
 * Created by Kamil Seweryn.
 */
public class MainActivity extends Activity {

    private static final String TAG_CHECK_PRICES_DIALOG = "CHECK_PRICES_DIALOG";

    @InjectView(R.id.sliding_tabs) SlidingTabLayout slidingTabs;
    @InjectView(R.id.form_switcher) ViewAnimator formSwitcher;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

        initFormSwitching();

        if (savedInstanceState == null && GeneralPreferences.isFirstLaunch())
            new CheckPricesDialogFragment()
                    .show(getFragmentManager(), TAG_CHECK_PRICES_DIALOG);
    }

    private void initFormSwitching() {
        formSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top));
        slidingTabs.setDistributeEvenly(true);
        slidingTabs.setViewPager(new ViewAnimatorToViewPagerAdapter(formSwitcher, new ProviderTabsProvider()));
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

    public void start(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
