package pl.srw.billcalculator.form.fragment;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.ProviderMapper;
import pl.srw.mfvp.di.scope.RetainFragmentScope;
import pl.srw.mfvp.presenter.MvpPresenter;

import static pl.srw.billcalculator.type.Provider.PGE;
import static pl.srw.billcalculator.type.Provider.PGNIG;
import static pl.srw.billcalculator.type.Provider.TAURON;

@RetainFragmentScope
public class FormPresenter extends MvpPresenter<FormPresenter.FormView> {

    private Provider provider;
    private ProviderMapper providerMapper;

    @Inject
    public FormPresenter(ProviderMapper providerMapper) {
        this.providerMapper = providerMapper;
    }

    public void setup(Provider provider) {
        this.provider = provider;
    }

    @Override
    protected void onFirstBind() {
        present(new UIChange<FormView>() {
            @Override
            public void change(FormView view) {
                view.setupSettingsLink();
                setFormValues(view);
                view.setDates(Dates.format(Dates.firstDayOfThisMonth()), Dates.format(Dates.lastDayOfThisMonth()));
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        present(new UIChange<FormView>() {
            @Override
            public void change(FormView view) {
                setFormValues(view);
            }
        });
    }

    public void settingsLinkClicked() {
        present(new UIChange<FormView>() {
            @Override
            public void change(FormView view) {
                view.showProviderSettings(provider);
            }
        });
    }

    @LayoutRes
    public int getFormLayout() {
        if (provider == PGNIG
                || SharedPreferencesEnergyPrices.TARIFF_G11.equals(getTariff())) {
            return R.layout.form;
        } else {
            return R.layout.form_g12;
        }
    }

    private void setFormValues(FormView view) {
        view.setLogo(provider);
        if (provider == PGE || provider == TAURON) {
            final String tariff = getTariff();
            view.setTariffText(tariff);
            view.setReadingUnit(R.string.form_reading_unit_kWh);
        } else {
            view.setReadingUnit(R.string.form_reading_unit_m3);
        }
    }

    private String getTariff() {
        return ((SharedPreferencesEnergyPrices)providerMapper.getPrices(provider)).getTariff();
    }

    public interface FormView {

        void showProviderSettings(Provider provider);

        void setLogo(Provider provider);

        void setupSettingsLink();

        void setTariffText(String tariff);

        void setDates(String fromDate, String toDate);

        void setReadingUnit(@StringRes int unitResId);
    }
}
