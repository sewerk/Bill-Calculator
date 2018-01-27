package pl.srw.billcalculator.settings.details.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import pl.srw.billcalculator.R
import pl.srw.billcalculator.databinding.SettingsInputDialogBinding
import pl.srw.billcalculator.settings.details.InputSettingsDetailsListItem

private const val ARG_DATA = "Settings.details.dialog.input.data"

class InputSettingsDialogFragment : DialogFragment() {

    companion object {
        fun show(activity: FragmentActivity,
                 item: InputSettingsDetailsListItem) {
            val bundle = Bundle()
            bundle.putParcelable(ARG_DATA, item)

            val dialogFragment = InputSettingsDialogFragment()
            dialogFragment.arguments = bundle
            dialogFragment.show(activity.supportFragmentManager, null)
        }
    }

    private val data by lazy { arguments!!.getParcelable(ARG_DATA) as InputSettingsDetailsListItem }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = LayoutInflater.from(context)
        val binding = SettingsInputDialogBinding.inflate(layoutInflater).apply {
            this.item = data
        }

        return AlertDialog.Builder(context!!)
                .setTitle(data.title)
                .setMessage(data.description?: R.string.empty)
                .setView(binding.root)
                .setPositiveButton(R.string.settings_input_accept, { _, _ -> TODO() })
                .setNegativeButton(R.string.settings_input_cancel, { _, _ -> dismiss() })
                .create()
    }
}
