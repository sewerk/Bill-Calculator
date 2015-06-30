package pl.srw.billcalculator.bill.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.PrintTask;
import pl.srw.billcalculator.util.ToWebView;

/**
 * Created by kseweryn on 18.06.15.
 */
public abstract class BillActivity extends BackableActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill, menu);
        return true;
    }

    @CallSuper
    @CheckResult
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View billView = findViewById(R.id.bill_content);
        if (item.getItemId() == R.id.action_zoom_out) {
            //TODO: consider loading icon, hide after done
            setContentView(ToWebView.wrapByWebView(this, billView));
            return true;
        } else if (item.getItemId() == R.id.action_print) {
            new PrintTask(billView, item).execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFile(final Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//TODO: check
        startActivity(intent);
    }
}
