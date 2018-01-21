package pl.srw.billcalculator.di

import android.content.Context
import pl.srw.billcalculator.BillCalculator
import pl.srw.billcalculator.bill.service.PgeBillStoringService
import pl.srw.billcalculator.bill.service.PgnigBillStoringService
import pl.srw.billcalculator.bill.service.TauronBillStoringService
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.history.di.HistoryComponent
import pl.srw.billcalculator.settings.details.SettingsDetailsController
import pl.srw.billcalculator.settings.details.SettingsDetailsVM
import pl.srw.billcalculator.settings.details.restore.ConfirmRestoreSettingsDialogFragment
import pl.srw.billcalculator.settings.di.SettingsComponent
import pl.srw.billcalculator.settings.list.SettingsController
import timber.log.Timber

@SuppressWarnings("TooManyFunctions")
object Dependencies {

    @JvmStatic
    lateinit var applicationComponent: ApplicationComponent
        private set

    private var historyComponent: HistoryComponent? = null
    private var settingsComponent: SettingsComponent? = null

    fun init(application: Context) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .build()
    }

    // APPLICATION SCOPE
    fun inject(application: BillCalculator) {
        applicationComponent.inject(application)
    }

    fun inject(service: PgeBillStoringService) {
        applicationComponent.inject(service)
    }

    fun inject(service: PgnigBillStoringService) {
        applicationComponent.inject(service)
    }

    fun inject(service: TauronBillStoringService) {
        applicationComponent.inject(service)
    }

    // ACTIVITIES

    // FRAGMENTS
    fun inject(fragment: FormFragment) {
        getHistoryComponent().formComponent.inject(fragment)
    }

    fun inject(controller: SettingsController) {
        getSettingsComponent().inject(controller)
    }

    fun inject(controller: SettingsDetailsController) {
        getSettingsComponent().inject(controller)
    }

    fun inject(fragment: ConfirmRestoreSettingsDialogFragment) {
        getSettingsComponent().inject(fragment)
    }

    // COMPONENTS MANAGEMENT
    fun getHistoryComponent(): HistoryComponent {
        if (historyComponent == null) {
            Timber.v("Creating HistoryComponent")
            historyComponent = applicationComponent.historyComponent
        }
        return historyComponent!!
    }

    fun releaseHistoryComponent() {
        Timber.v("Releasing HistoryComponent")
        historyComponent = null
    }

    fun getSettingsComponent(): SettingsComponent {
        if (settingsComponent == null) {
            Timber.v("Creating SettingsComponent")
            settingsComponent = applicationComponent.settingsComponent
        }
        return settingsComponent!!
    }

    fun releaseSettingsComponent() {
        Timber.v("Releasing SettingsComponent")
        settingsComponent = null
    }

    fun set(vm: SettingsDetailsVM) {
        getSettingsComponent().module().settingsDetailsVM = vm
    }
}
