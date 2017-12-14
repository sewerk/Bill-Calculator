package pl.srw.billcalculator.history.di

import dagger.Module
import dagger.Provides
import pl.srw.billcalculator.form.fragment.FormPresenter
import pl.srw.billcalculator.history.HistoryPresenter
import pl.srw.mfvp.di.scope.RetainActivityScope

@Module
class HistoryModule {

    @Provides
    @RetainActivityScope
    fun providesHistoryUpdating(presenter: HistoryPresenter): FormPresenter.HistoryChangeListener {
        return presenter
    }
}
