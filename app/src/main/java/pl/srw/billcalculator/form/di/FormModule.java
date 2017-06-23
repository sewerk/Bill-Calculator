package pl.srw.billcalculator.form.di;

import dagger.Module;
import dagger.Provides;
import pl.srw.billcalculator.form.fragment.FormPresenter;
import pl.srw.billcalculator.history.HistoryPresenter;
import pl.srw.mfvp.di.scope.RetainFragmentScope;

@Module
public class FormModule {

    @Provides
    @RetainFragmentScope
    FormPresenter.HistoryUpdating providesHistoryUpdating(HistoryPresenter presenter) {
        return presenter;
    }
}
