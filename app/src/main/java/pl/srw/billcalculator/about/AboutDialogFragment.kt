package pl.srw.billcalculator.about

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.*
import pl.srw.billcalculator.BuildConfig
import pl.srw.billcalculator.R
import pl.srw.billcalculator.history.di.HistoryComponent
import pl.srw.billcalculator.type.ContentType
import pl.srw.billcalculator.wrapper.Analytics
import pl.srw.mfvp.MvpFragment
import pl.srw.mfvp.di.MvpActivityScopedFragment

private const val RECEIVER_EMAIL = "kalkulator.rachunkow@gmail.com"
private const val BETA_HTTP = "https://plus.google.com/communities/113263640175495853700"

class AboutDialogFragment : MvpFragment(), MvpActivityScopedFragment<HistoryComponent> {

    private lateinit var unbinder: Unbinder

    @BindView(R.id.tv_ver) lateinit var tvVersion: TextView
    @BindView(R.id.tv_link_emailme) lateinit var tvLinkEmail: TextView
    @BindView(R.id.tv_link_g_plus) lateinit var tvLinkGPlus: TextView
    @BindString(R.string.app_name) lateinit var appName: String
    @BindString(R.string.emailme) lateinit var emailMeTitle: String
    @BindString(R.string.email_client_missing) lateinit var noEmailClientMsg: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Analytics.logContent(ContentType.ABOUT)
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

    override fun injectDependencies(component: HistoryComponent) {
        // no injections needed
    }

    private fun setLinks() {
        setEmailMeLink()
        setGPlusLink()
        setLicenseLink()
    }

    private fun setEmailMeLink() {
        val underline1 = "<a href=\"\">" + tvLinkEmail.text + "</a>"
        tvLinkEmail.text = Html.fromHtml(underline1)
    }

    private fun setGPlusLink() {
        val underline = "<a href=\"" + BETA_HTTP + "\">" + tvLinkGPlus.text + "</a>"
        tvLinkGPlus.text = Html.fromHtml(underline)
        tvLinkGPlus.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setLicenseLink() {
        tvVersion.text = Html.fromHtml(getString(R.string.version_text, BuildConfig.VERSION_NAME))
        tvVersion.movementMethod = LinkMovementMethod.getInstance()
    }

    @OnClick(R.id.tv_link_emailme)
    fun sendEmail() {
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
        dismiss()
    }
}