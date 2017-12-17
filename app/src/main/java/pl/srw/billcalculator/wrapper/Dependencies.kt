package pl.srw.billcalculator.wrapper

import android.content.Context

import pl.srw.billcalculator.BillCalculator
import pl.srw.billcalculator.bill.service.PgeBillStoringService
import pl.srw.billcalculator.bill.service.PgnigBillStoringService
import pl.srw.billcalculator.bill.service.TauronBillStoringService
import pl.srw.billcalculator.di.ApplicationComponent
import pl.srw.billcalculator.di.ApplicationModule
import pl.srw.billcalculator.di.DaggerApplicationComponent
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.history.di.HistoryComponent
import timber.log.Timber

object Dependencies {

    @JvmStatic
    lateinit var applicationComponent: ApplicationComponent
        private set

    private var historyComponent: HistoryComponent? = null

    fun init(application: Context) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .build()
    }

    // APLICATION SCOPE
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
}
