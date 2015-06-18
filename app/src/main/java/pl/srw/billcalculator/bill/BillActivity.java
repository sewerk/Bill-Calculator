package pl.srw.billcalculator.bill;

import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.BuildConfig;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.util.ToWebView;

/**
 * Created by kseweryn on 18.06.15.
 */
public class BillActivity extends BackableActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEVEL) //TODO: experimental feature
            getMenuInflater().inflate(R.menu.bill, menu);
        return true;
    }

    @CallSuper
    @CheckResult
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_zoom_out) {
            //TODO: consider loading icon, hide after done
            View billView = findViewById(R.id.bill_content);
            setContentView(ToWebView.wrapByWebView(this, billView));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
