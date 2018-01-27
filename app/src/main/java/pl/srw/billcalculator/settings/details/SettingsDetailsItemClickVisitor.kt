package pl.srw.billcalculator.settings.details

import android.support.v4.app.FragmentActivity
import com.bluelinelabs.conductor.Router
import pl.srw.billcalculator.settings.details.dialog.InputSettingsDialogFragment

class SettingsDetailsItemClickVisitor(private val router: Router) {

    fun visit(item: PickingSettingsDetailsListItem) {
        TODO("not implemented")
    }

    fun visit(item: InputSettingsDetailsListItem) {
        InputSettingsDialogFragment.show(router.activity as FragmentActivity, item)
    }
}
