package pl.srw.billcalculator.settings.restore;

import javax.inject.Inject;

import pl.srw.billcalculator.settings.prices.RestorablePrices;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.ProviderMapper;
import pl.srw.mfvp.MvpPresenter;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
public class ConfirmRestoreSettingsPresenter extends MvpPresenter<ConfirmRestoreSettingsPresenter.ConfirmRestoreSettingsView> {

    private final ProviderMapper providerMapper;
    private RestorablePrices prices;

    @Inject
    public ConfirmRestoreSettingsPresenter(ProviderMapper providerMapper) {
        this.providerMapper = providerMapper;
    }

    public void setup(Provider provider) {
        prices = providerMapper.getPrices(provider);
    }

    public void onConfirmedClicked() {
        prices.setDefault();
        present(ConfirmRestoreSettingsView::refresh);
    }

    public interface ConfirmRestoreSettingsView {

        void refresh();
    }
}
