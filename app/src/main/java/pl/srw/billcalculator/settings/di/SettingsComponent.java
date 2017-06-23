package pl.srw.billcalculator.settings.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.settings.activity.SettingsActivity;
import pl.srw.billcalculator.settings.fragment.PgeSettingsFragment;
import pl.srw.billcalculator.settings.fragment.PgnigSettingsFragment;
import pl.srw.billcalculator.settings.fragment.TauronSettingsFragment;
import pl.srw.billcalculator.settings.restore.ConfirmRestoreSettingsDialogFragment;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
@Subcomponent
public interface SettingsComponent extends ConfirmRestoreSettingsComponentInjectable<SettingsActivity> {

    void inject(ConfirmRestoreSettingsDialogFragment fragment);

    void inject(PgeSettingsFragment pgeSettingsFragment);

    void inject(PgnigSettingsFragment pgnigSettingsFragment);

    void inject(TauronSettingsFragment tauronSettingsFragment);
}
