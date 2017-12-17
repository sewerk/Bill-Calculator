package pl.srw.billcalculator.form.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.form.fragment.FormFragment;

@Subcomponent(modules = FormModule.class)
public interface FormComponent {

    void inject(FormFragment fragment);
}
