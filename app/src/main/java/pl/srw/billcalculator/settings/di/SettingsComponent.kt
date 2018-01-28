package pl.srw.billcalculator.settings.di

import dagger.Subcomponent
import pl.srw.billcalculator.settings.activity.SettingsActivity
import pl.srw.billcalculator.settings.details.SettingsDetailsController
import pl.srw.billcalculator.settings.details.dialog.InputSettingsDialogFragment
import pl.srw.billcalculator.settings.details.restore.ConfirmRestoreSettingsDialogFragment
import pl.srw.billcalculator.settings.fragment.PgeSettingsFragment
import pl.srw.billcalculator.settings.fragment.PgnigSettingsFragment
import pl.srw.billcalculator.settings.fragment.TauronSettingsFragment
import pl.srw.billcalculator.settings.list.SettingsController
import pl.srw.mfvp.di.MvpComponent
import pl.srw.mfvp.di.scope.RetainActivityScope

@RetainActivityScope
@Subcomponent
interface SettingsComponent : MvpComponent<SettingsActivity> {

    fun inject(pgeSettingsFragment: PgeSettingsFragment)

    fun inject(pgnigSettingsFragment: PgnigSettingsFragment)

    fun inject(tauronSettingsFragment: TauronSettingsFragment)

    fun inject(controller: SettingsController)

    fun inject(controller: SettingsDetailsController)

    fun inject(dialog: ConfirmRestoreSettingsDialogFragment)

    fun inject(dialog: InputSettingsDialogFragment)
}
