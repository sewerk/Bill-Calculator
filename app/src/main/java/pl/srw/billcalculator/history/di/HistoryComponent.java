package pl.srw.billcalculator.history.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.dialog.CheckPricesDialogFragment;
import pl.srw.billcalculator.form.di.FormComponent;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.history.NewUIDialogFragment;
import pl.srw.mfvp.di.MvpComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
@Subcomponent(modules = HistoryModule.class)
public interface HistoryComponent extends MvpComponent<DrawerActivity> {

    FormComponent getFormComponent();

    void inject(CheckPricesDialogFragment dialogFragment);

    void inject(NewUIDialogFragment dialogFragment);
}
