package pl.srw.billcalculator.about

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import pl.srw.billcalculator.BuildConfig
import pl.srw.billcalculator.R
import pl.srw.billcalculator.util.analytics.Analytics
import pl.srw.billcalculator.util.analytics.ContentType
import pl.srw.billcalculator.util.analytics.EventType
import timber.log.Timber

private const val RECEIVER_EMAIL = "kalkulator.rachunkow@gmail.com"
private const val BETA_HTTP = "https://plus.google.com/communities/113263640175495853700"

class AboutDialogFragment : DialogFragment() {

    private lateinit var unbinder: Unbinder

    @BindView(R.id.tv_ver) lateinit var tvVersion: TextView
    @BindView(R.id.tv_link_emailme) lateinit var tvLinkEmail: TextView
    @BindView(R.id.tv_link_g_plus) lateinit var tvLinkGPlus: TextView
    @BindString(R.string.app_name) lateinit var appName: String
    @BindString(R.string.emailme) lateinit var emailMeTitle: String
    @BindString(R.string.email_client_missing) lateinit var noEmailClientMsg: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Analytics.contentView(ContentType.ABOUT)
        val view = View.inflate(context, R.layout.about, null)
        val dialog = AlertDialog.Builder(activity)
                .setTitle(R.string.about_label)
                .setView(view)
                .create()
        unbinder = ButterKnife.bind(this, view)
        setLinks()
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    private fun setLinks() {
        setEmailMeLink()
        setGPlusLink()
        setLicenseLink()
    }

    private fun setEmailMeLink() {
        val underline1 = "<a href=\"\">" + tvLinkEmail.text + "</a>"
        @Suppress("DEPRECATION")
        tvLinkEmail.text = Html.fromHtml(underline1)
    }

    private fun setGPlusLink() {
        val underline = "<a href=\"" + BETA_HTTP + "\">" + tvLinkGPlus.text + "</a>"
        @Suppress("DEPRECATION")
        tvLinkGPlus.text = Html.fromHtml(underline)
        tvLinkGPlus.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setLicenseLink() {
        @Suppress("DEPRECATION")
        tvVersion.text = Html.fromHtml(getString(R.string.version_text, BuildConfig.VERSION_NAME))
        tvVersion.movementMethod = LinkMovementMethod.getInstance()
    }

    @OnClick(R.id.tv_link_g_plus)
    fun onGooglePlusLinkClicked() {
        Analytics.event(EventType.CONTACT, "action", "gPlus")
    }

    @OnClick(R.id.tv_link_emailme)
    fun sendEmail() {
        Analytics.event(EventType.CONTACT, "action", "send mail")
        val i = Intent(Intent.ACTION_SENDTO)
        i.data = Uri.parse("mailto:" + RECEIVER_EMAIL)
        i.putExtra(Intent.EXTRA_SUBJECT, appName)
        try {
            startActivity(Intent.createChooser(i, emailMeTitle))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(activity, noEmailClientMsg, Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.b_close)
    fun closeAboutDialog() {
        Timber.i("About: close button")
        dismiss()
    }
}
