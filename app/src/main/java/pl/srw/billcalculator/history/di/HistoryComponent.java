package pl.srw.billcalculator.history.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.form.di.FormComponent;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
@Subcomponent
public interface HistoryComponent extends MvpActivityScopeComponent<DrawerActivity> {

    FormComponent getFormComponent();
}