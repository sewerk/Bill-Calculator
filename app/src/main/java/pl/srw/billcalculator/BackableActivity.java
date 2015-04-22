package pl.srw.billcalculator;

import android.os.Bundle;
import android.view.MenuItem;

import com.f2prateek.dart.Dart;

import butterknife.ButterKnife;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusActivity;

/**
 * Created by Kamil Seweryn.
 */
public abstract class BackableActivity<T extends Presenter> extends NucleusActivity<T> {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dart.inject(this);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

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
