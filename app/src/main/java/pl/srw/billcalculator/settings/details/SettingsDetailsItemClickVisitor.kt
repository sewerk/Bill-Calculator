package pl.srw.billcalculator.settings.details

import android.support.v4.app.FragmentActivity
import pl.srw.billcalculator.settings.details.dialog.InputSettingsDialogFragment
import pl.srw.billcalculator.settings.details.dialog.PickingSettingsDialogFragment

class SettingsDetailsItemClickVisitor(private val activity: FragmentActivity) {

    fun visit(item: PickingSettingsDetailsListItem) {
        PickingSettingsDialogFragment.show(activity, item)
    }

    fun visit(item: InputSettingsDetailsListItem) {
        InputSettingsDialogFragment.show(activity, item)
    }
}
