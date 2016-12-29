package pl.srw.billcalculator;

import android.os.Bundle;
import android.view.MenuItem;

import butterknife.ButterKnife;
import pl.srw.mfvp.MvpActivity;
import pl.srw.mfvp.di.component.MvpActivityScopeComponent;

/**
 * Created by Kamil Seweryn.
 */
public abstract class BackableActivity<T extends MvpActivityScopeComponent> extends MvpActivity<T> {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
