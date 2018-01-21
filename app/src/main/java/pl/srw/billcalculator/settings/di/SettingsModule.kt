package pl.srw.billcalculator.settings.di

import dagger.Module
import dagger.Provides
import pl.srw.billcalculator.settings.details.SettingsDetailsVM

@Module
class SettingsModule {

    lateinit var settingsDetailsVM: SettingsDetailsVM
        @Provides get

    @Provides fun getModule() = this
}
