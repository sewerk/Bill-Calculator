package pl.srw.billcalculator.settings.restore;

import javax.inject.Inject;

import dagger.Lazy;
import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.settings.prices.RestorablePrices;
import pl.srw.billcalculator.settings.prices.TauronPrices;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.presenter.MvpPresenter;

@RetainActivityScope
public class ConfirmRestoreSettingsPresenter extends MvpPresenter<ConfirmRestoreSettingsPresenter.ConfirmRestoreSettingsView> {

    private RestorablePrices prices;

    private final Lazy<PgePrices> pgePricesLazy;
    private final Lazy<PgnigPrices> pgnigPricesLazy;
    private final Lazy<TauronPrices> tauronPricesLazy;

    @Inject
    public ConfirmRestoreSettingsPresenter(Lazy<PgePrices> pgePricesLazy, Lazy<PgnigPrices> pgnigPricesLazy,
                                           Lazy<TauronPrices> tauronPricesLazy) {
        this.pgePricesLazy = pgePricesLazy;
        this.pgnigPricesLazy = pgnigPricesLazy;
        this.tauronPricesLazy = tauronPricesLazy;
    }

    public void setup(Provider provider) {
        switch (provider) {
            case PGE:
                prices = pgePricesLazy.get();
                break;
            case PGNIG:
                prices = pgnigPricesLazy.get();
                break;
            case TAURON:
                prices = tauronPricesLazy.get();
                break;
            default:
                throw new EnumVariantNotHandledException(provider);
        }
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
