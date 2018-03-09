package pl.srw.billcalculator.settings.details.dialog

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.bundleOf
import pl.srw.billcalculator.databinding.SettingsInputDialogBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.InputSettingsDetailsListItem
import pl.srw.billcalculator.settings.details.SettingsDetailsVM
import pl.srw.billcalculator.settings.details.SettingsDetailsVMFactory
import javax.inject.Inject

private const val ARG_DATA = "Settings.details.dialog.input.data"
private const val HTML_PATTERN_TO_HANDLE = "href"

class InputSettingsDialogFragment : DialogFragment() {

    companion object {
        fun show(
            activity: FragmentActivity,
            item: InputSettingsDetailsListItem
        ) {
            val dialogFragment = InputSettingsDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_DATA, item)
            dialogFragment.show(activity.supportFragmentManager, null)
        }
    }

    @Inject lateinit var vmFactory: SettingsDetailsVMFactory

    private val data by lazy { arguments!!.getParcelable(ARG_DATA) as InputSettingsDetailsListItem }
    private val vm by lazy { ViewModelProviders.of(activity!!, vmFactory).get(SettingsDetailsVM::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = LayoutInflater.from(context)
        val binding = SettingsInputDialogBinding.inflate(layoutInflater).apply {
            this.value = data.value
            this.measureId = data.measure
        }

        val description = getStringDescription(data.description)
        return buildDialog(description, binding)
            .also { it.showKeyboard() }
    }

    override fun onResume() {
        super.onResume()
        moveInputCursorToEnd()
        // setting movement link clickable need to happen after show()
        makeMessageClickable(dialog)
    }

    private fun buildDialog(
        description: CharSequence?,
        binding: SettingsInputDialogBinding
    ) = AlertDialog.Builder(context!!)
        .setTitle(data.title)
        .setMessage(description)
        .setView(binding.root)
        .setPositiveButton(R.string.settings_input_accept, { _, _ -> vm.valueChanged(data.title, binding.value!!) })
        .setNegativeButton(R.string.settings_input_cancel, { _, _ -> dismiss() })
        .create()

    private fun getStringDescription(resId: Int?): CharSequence? {
        val description = resId?.let { getString(it) }
        return convertFromHtmlIfNecessary(description).first
    }

    private fun convertFromHtmlIfNecessary(description: String?): Pair<CharSequence?, Boolean> =
        if (description != null && description.contains(HTML_PATTERN_TO_HANDLE)) {
            @Suppress("DEPRECATION")
            Html.fromHtml(description) to true
        } else description to false

    private fun makeMessageClickable(dialog: Dialog) {
        val textView = dialog.findViewById<TextView>(android.R.id.message)
        if (textView?.text is Spanned) textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun moveInputCursorToEnd() {
        getInputView().apply {
            post { moveSelectionToEndIfAtBeginning() }
        }
    }

    private fun getInputView() = dialog.findViewById<EditText>(R.id.settingsDialogInput)

    private fun EditText.moveSelectionToEndIfAtBeginning() {
        if (selectionEnd == 0) setSelection(text.length)
    }

    private fun AlertDialog.showKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}
