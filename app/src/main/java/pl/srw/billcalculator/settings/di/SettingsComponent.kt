package pl.srw.billcalculator.settings.di

import dagger.Subcomponent
import pl.srw.billcalculator.settings.SettingsActivity
import pl.srw.billcalculator.settings.details.SettingsDetailsFragment
import pl.srw.billcalculator.settings.details.dialog.InputSettingsDialogFragment
import pl.srw.billcalculator.settings.details.dialog.PickingSettingsDialogFragment
import pl.srw.billcalculator.settings.details.restore.ConfirmRestoreSettingsDialogFragment
import pl.srw.billcalculator.settings.list.SettingsFragment
import pl.srw.mfvp.di.scope.RetainActivityScope

@RetainActivityScope
@Subcomponent
interface SettingsComponent {

    fun inject(activity: SettingsActivity)

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: SettingsDetailsFragment)

    fun inject(dialog: ConfirmRestoreSettingsDialogFragment)

    fun inject(dialog: InputSettingsDialogFragment)

    fun inject(dialog: PickingSettingsDialogFragment)
}
