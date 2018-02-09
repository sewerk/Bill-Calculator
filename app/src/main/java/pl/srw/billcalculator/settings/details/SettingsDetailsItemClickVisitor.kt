package pl.srw.billcalculator.settings.details

import android.support.v4.app.FragmentActivity
import com.bluelinelabs.conductor.Router
import pl.srw.billcalculator.settings.details.dialog.InputSettingsDialogFragment
import pl.srw.billcalculator.settings.details.dialog.PickingSettingsDialogFragment

class SettingsDetailsItemClickVisitor(private val router: Router) {

    fun visit(item: PickingSettingsDetailsListItem) {
        PickingSettingsDialogFragment.show(router.activity as FragmentActivity, item)
    }

    fun visit(item: InputSettingsDetailsListItem) {
        InputSettingsDialogFragment.show(router.activity as FragmentActivity, item)
    }
}
