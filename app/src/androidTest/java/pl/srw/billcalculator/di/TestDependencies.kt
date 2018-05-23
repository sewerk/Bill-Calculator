package pl.srw.billcalculator.di

import pl.srw.billcalculator.bill.activity.AbstractVerifyBillCreationUITest
import pl.srw.billcalculator.form.view.InstantAutoCompleteUITest
import pl.srw.billcalculator.history.HistoryUITest
import pl.srw.billcalculator.settings.SettingsUITest

/**
 * Setup test component to access production instances
 */
object TestDependencies {

    private val component = DaggerTestApplicationComponent.builder()
        .testApplicationModule(TestApplicationModule(Dependencies.applicationComponent))
        .build()

    fun inject(test: HistoryUITest) {
        component.inject(test)
    }

    fun inject(test: InstantAutoCompleteUITest) {
        component.inject(test)
    }

    fun inject(test: SettingsUITest) {
        component.inject(test)
    }

    fun inject(test: AbstractVerifyBillCreationUITest) {
        component.inject(test)
    }
}
