package pl.srw.billcalculator.form.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.form.fragment.FormFragment;
import pl.srw.mfvp.di.component.MvpFragmentScopeComponent;
import pl.srw.mfvp.di.scope.RetainFragmentScope;

@RetainFragmentScope
@Subcomponent(modules = FormModule.class)
public interface FormComponent extends MvpFragmentScopeComponent<FormFragment> {
}
