package pl.srw.billcalculator.di

import dagger.Component
import pl.srw.billcalculator.bill.activity.AbstractVerifyBillCreationUITest
import pl.srw.billcalculator.form.view.InstantAutoCompleteUITest
import pl.srw.billcalculator.history.HistoryUITest
import pl.srw.billcalculator.settings.SettingsUITest
import javax.inject.Singleton

@Singleton
@Component(modules = [TestApplicationModule::class])
interface TestApplicationComponent {

    fun inject(test: InstantAutoCompleteUITest)

    fun inject(settingsUITest: SettingsUITest)

    fun inject(historyUITest: HistoryUITest)

    fun inject(historyUITest: AbstractVerifyBillCreationUITest)
}
