package pl.srw.billcalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.wrapper.Analytics;

public class AboutActivity extends AppCompatActivity {

    private static final String RECEIVER_EMAIL = "kalkulator.rachunkow@gmail.com";
    private static final String BETA_HTTP = "https://plus.google.com/communities/113263640175495853700";

    @BindView(R.id.tv_ver) TextView tvVersion;
    @BindView(R.id.tv_link_emailme) TextView tvLinkEmail;
    @BindView(R.id.tv_link_g_plus) TextView tvLinkGPlus;
    @BindString(R.string.app_name) String appName;
    @BindString(R.string.emailme) String emailMeTitle;
    @BindString(R.string.email_client_missing) String noEmailClientMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Analytics.logContent(ContentType.ABOUT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ButterKnife.bind(this);

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
        tvVersion.setText(Html.fromHtml(getString(R.string.version_text, BuildConfig.VERSION_NAME)));
        tvVersion.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick(R.id.tv_link_emailme)
    public void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:" + RECEIVER_EMAIL));
        i.putExtra(Intent.EXTRA_SUBJECT, appName);
        try {
            startActivity(Intent.createChooser(i, emailMeTitle));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, noEmailClientMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.b_close)
    public void closeAboutDialog() {
        onBackPressed();
    }
}