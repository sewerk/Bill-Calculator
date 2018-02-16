package pl.srw.billcalculator.settings.details.dialog

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.bundleOf
import pl.srw.billcalculator.databinding.SettingsInputDialogBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.InputSettingsDetailsListItem
import pl.srw.billcalculator.settings.details.SettingsDetailsVM
import pl.srw.billcalculator.settings.details.SettingsDetailsVMFactory
import javax.inject.Inject

private const val ARG_DATA = "Settings.details.dialog.input.data"

class InputSettingsDialogFragment : DialogFragment() {

    companion object {
        fun show(activity: FragmentActivity,
                 item: InputSettingsDetailsListItem) {

            val dialogFragment = InputSettingsDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_DATA, item)
            dialogFragment.show(activity.supportFragmentManager, null)
        }
    }

    @Inject lateinit var vmFactory: SettingsDetailsVMFactory

    private val data by lazy { arguments!!.getParcelable(ARG_DATA) as InputSettingsDetailsListItem }
    private val vm by lazy { ViewModelProviders.of(activity!!, vmFactory).get(SettingsDetailsVM::class.java)}

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

        return AlertDialog.Builder(context!!)
                .setTitle(data.title)
                .setMessage(data.description?: R.string.empty)
                .setView(binding.root)
                .setPositiveButton(R.string.settings_input_accept, { _, _ -> vm.valueChanged(data.title, binding.value!!) })
                .setNegativeButton(R.string.settings_input_cancel, { _, _ -> dismiss() })
                .create()
    }
}
