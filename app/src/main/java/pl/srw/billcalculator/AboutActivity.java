package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Kamil Seweryn
 */
public class AboutActivity extends Activity {

    public static final String RECEIVER_EMAIL = "sewerk@gmail.com";

    @InjectView(R.id.textView_ver) TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.inject(this);

        setLicenseLink();
    }

    private void setLicenseLink() {
        tvVersion.setText(Html.fromHtml(getString(R.string.version_text, getApkVersion())));
        tvVersion.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String getApkVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @OnClick(R.id.textView_emalme)
    public void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:" + RECEIVER_EMAIL));
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.emailme)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.email_client_missing), Toast.LENGTH_SHORT).show();
        }
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
