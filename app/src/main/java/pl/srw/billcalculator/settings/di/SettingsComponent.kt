package pl.srw.billcalculator.settings.di

import dagger.Subcomponent
import pl.srw.billcalculator.settings.SettingsController
import pl.srw.billcalculator.settings.activity.SettingsActivity
import pl.srw.billcalculator.settings.details.SettingsDetailsController
import pl.srw.billcalculator.settings.details.restore.ConfirmRestoreSettingsDialogFragment
import pl.srw.billcalculator.settings.fragment.PgeSettingsFragment
import pl.srw.billcalculator.settings.fragment.PgnigSettingsFragment
import pl.srw.billcalculator.settings.fragment.TauronSettingsFragment
import pl.srw.mfvp.di.MvpComponent
import pl.srw.mfvp.di.scope.RetainActivityScope

@RetainActivityScope
@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent : MvpComponent<SettingsActivity> {

    fun inject(fragment: ConfirmRestoreSettingsDialogFragment)

    fun inject(pgeSettingsFragment: PgeSettingsFragment)

    fun inject(pgnigSettingsFragment: PgnigSettingsFragment)

    fun inject(tauronSettingsFragment: TauronSettingsFragment)

    fun inject(controller: SettingsController)

    fun inject(controller: SettingsDetailsController)

    fun module(): SettingsModule
}
