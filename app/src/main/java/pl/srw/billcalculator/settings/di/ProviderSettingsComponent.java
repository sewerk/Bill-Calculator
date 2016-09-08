package pl.srw.billcalculator.settings.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
@Subcomponent
public interface ProviderSettingsComponent extends ConfirmRestoreSettingsComponentInjectable<ProviderSettingsActivity> {
}
