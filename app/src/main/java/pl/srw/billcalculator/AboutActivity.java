package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Kamil Seweryn
 */
public class AboutActivity extends Activity {

    public static final String RECEIVER_EMAIL = "kalkulator.rachunkow@gmail.com";
    public static final String BETA_HTTP = "https://plus.google.com/communities/113263640175495853700";

    @InjectView(R.id.tv_ver) TextView tvVersion;
    @InjectView(R.id.tv_link_emailme) TextView tvLinkEmail;
    @InjectView(R.id.tv_link_g_plus) TextView tvLinkGPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ButterKnife.inject(this);

        setLinks();
    }

    private void setLinks() {
        setEmailMeLink();
        setGPlusLink();
        setLicenseLink();
    }

    private void setEmailMeLink() {
        String underline1 = "<a href=\"\">" + tvLinkEmail.getText() + "</a>";
        tvLinkEmail.setText(Html.fromHtml(underline1));
    }

    private void setGPlusLink() {
        String underline = "<a href=\"" + BETA_HTTP + "\">" + tvLinkGPlus.getText() + "</a>";
        tvLinkGPlus.setText(Html.fromHtml(underline));
        tvLinkGPlus.setMovementMethod(LinkMovementMethod.getInstance());
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

    @OnClick(R.id.tv_link_emailme)
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

    @OnClick(R.id.b_close)
    public void closeAboutDialog() {
        onBackPressed();
    }
}
