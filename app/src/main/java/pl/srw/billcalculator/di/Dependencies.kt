package pl.srw.billcalculator.di

import android.content.Context
import android.os.Looper
import pl.srw.billcalculator.BillCalculator
import pl.srw.billcalculator.dialog.CheckPricesDialogFragment
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.history.NewUIDialogFragment
import pl.srw.billcalculator.history.di.HistoryComponent
import pl.srw.billcalculator.settings.details.SettingsDetailsFragment
import pl.srw.billcalculator.settings.details.dialog.InputSettingsDialogFragment
import pl.srw.billcalculator.settings.details.dialog.PickingSettingsDialogFragment
import pl.srw.billcalculator.settings.details.restore.ConfirmRestoreSettingsDialogFragment
import pl.srw.billcalculator.settings.di.SettingsComponent
import pl.srw.billcalculator.settings.list.SettingsFragment
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

    // ACTIVITIES

    // FRAGMENTS
    fun inject(fragment: FormFragment) {
        getHistoryComponent().formComponent.inject(fragment)
    }

    fun inject(dialog: CheckPricesDialogFragment) {
        getHistoryComponent().inject(dialog)
    }

    fun inject(dialog: NewUIDialogFragment) {
        getHistoryComponent().inject(dialog)
    }

    fun inject(fragment: SettingsFragment) {
        getSettingsComponent().inject(fragment)
    }

    fun inject(fragment: SettingsDetailsFragment) {
        getSettingsComponent().inject(fragment)
    }

    fun inject(dialog: ConfirmRestoreSettingsDialogFragment) {
        getSettingsComponent().inject(dialog)
    }

    fun inject(dialog: InputSettingsDialogFragment) {
        getSettingsComponent().inject(dialog)
    }

    fun inject(dialog: PickingSettingsDialogFragment) {
        getSettingsComponent().inject(dialog)
    }

    // COMPONENTS MANAGEMENT
    fun getHistoryComponent(): HistoryComponent {
        checkMainThread()
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

    private fun getSettingsComponent(): SettingsComponent {
        checkMainThread()
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

    // OTHER

    private fun checkMainThread() {
        if (!isMainThread()) throw IllegalStateException("This method should be executed on main thread!")
    }

    private fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }
}
