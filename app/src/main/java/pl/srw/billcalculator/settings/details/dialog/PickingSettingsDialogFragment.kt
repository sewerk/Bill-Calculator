package pl.srw.billcalculator.settings.details.dialog

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.LinearLayout
import pl.srw.billcalculator.databinding.SettingsPickingDialogItemBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.PickingSettingsDetailsListItem
import pl.srw.billcalculator.settings.details.SettingsDetailsVM
import pl.srw.billcalculator.settings.details.SettingsDetailsVMFactory
import javax.inject.Inject

private const val ARG_DATA = "Settings.details.dialog.picking.data"

class PickingSettingsDialogFragment : DialogFragment() {

    companion object {
        fun show(activity: FragmentActivity,
                 item: PickingSettingsDetailsListItem) {
            val bundle = Bundle()
            bundle.putParcelable(ARG_DATA, item)

            val dialogFragment = PickingSettingsDialogFragment()
            dialogFragment.arguments = bundle
            dialogFragment.show(activity.supportFragmentManager, null)
        }
    }

    @Inject lateinit var vmFactory: SettingsDetailsVMFactory

    private val data by lazy { arguments!!.getParcelable(ARG_DATA) as PickingSettingsDetailsListItem }
    private val vm by lazy { ViewModelProviders.of(activity!!, vmFactory).get(SettingsDetailsVM::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setTitle(data.title)
            .setView(prepareListView(data.options))
            .create()
    }

    private fun prepareListView(options: List<Int>): LinearLayout {
        val layoutInflater = LayoutInflater.from(context)
        val group = LinearLayout(context)
        group.orientation = LinearLayout.VERTICAL
        for (optionTextId in options) {
            SettingsPickingDialogItemBinding.inflate(layoutInflater, group, true).apply {
                textId = optionTextId
                isChecked = optionTextId == data.value
            }.root.setOnClickListener {
                vm.optionPicked(data.title, optionTextId)
                dismiss()
            }
        }
        return group
    }
}
