package pl.srw.billcalculator.bill;

import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.BuildConfig;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.util.ToWebView;
import java.io.File;

import pl.srw.billcalculator.util.PdfGenerator;

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
        if (item.getItemId() == R.id.action_zoom_out) {
            //TODO: consider loading icon, hide after done
            View billView = findViewById(R.id.bill_content);
            setContentView(ToWebView.wrapByWebView(this, billView));
            return true;
        } else if (item.getItemId() == R.id.action_print) {
            //TODO: async, pick location
            String filePath = PdfGenerator.generateFrom(findViewById(R.id.bill_content), "bill");
            if (filePath == null)
                Toast.makeText(this, "Saving failed", Toast.LENGTH_SHORT).show();
            else
                fireOpenFileIntent(Uri.fromFile(new File(filePath)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void fireOpenFileIntent(final Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//TODO: check
        startActivity(intent);
    }
}
