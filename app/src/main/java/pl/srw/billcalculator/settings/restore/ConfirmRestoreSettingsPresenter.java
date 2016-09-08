package pl.srw.billcalculator.settings.restore;

import javax.inject.Inject;

import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.settings.prices.RestorablePrices;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.ProviderMapper;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.presenter.MvpPresenter;

@RetainActivityScope
public class ConfirmRestoreSettingsPresenter extends MvpPresenter<ConfirmRestoreSettingsPresenter.ConfirmRestoreSettingsView> {

    private RestorablePrices prices;
    private ProviderMapper providerMapper;

    @Inject
    public ConfirmRestoreSettingsPresenter(ProviderMapper providerMapper) {
        this.providerMapper = providerMapper;
    }

    public void setup(Provider provider) {
        prices = providerMapper.getPrices(provider);
    }

    public void onConfirmedClicked() {
        prices.setDefault();
        AnalyticsWrapper.logAction(ActionType.RESTORE_PRICES, "Default prices restored", prices.getClass().getSimpleName());
        present(new UIChange<ConfirmRestoreSettingsView>() {
            @Override
            public void change(ConfirmRestoreSettingsView view) {
                view.refresh();
            }
        });
    }

    public interface ConfirmRestoreSettingsView {

        void refresh();
    }
}
