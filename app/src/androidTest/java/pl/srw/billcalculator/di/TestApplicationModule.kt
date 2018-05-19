package pl.srw.billcalculator.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class TestApplicationModule(private val productionComponent: ApplicationComponent) {

    @Provides @Singleton
    fun providePgePrices() = productionComponent.pgePrices

    @Provides @Singleton
    fun provideApplicationRepo() = productionComponent.applicationRepo

    @Provides @Singleton
    fun provideBillSelection() = productionComponent.billSelection

    @Provides @Singleton
    fun providePricesRepo() = productionComponent.pricesRepo

    @Provides @Singleton
    fun provideHistoryRepo() = productionComponent.historyRepo

    @Provides @Singleton
    fun applicationContext() = productionComponent.context
}
