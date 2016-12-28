package pl.srw.billcalculator.form.fragment;

import android.support.annotation.StringRes;
import android.view.View;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.FormValueValidator;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.ProviderMapper;
import pl.srw.mfvp.di.scope.RetainFragmentScope;
import pl.srw.mfvp.presenter.MvpPresenter;

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;
import static pl.srw.billcalculator.type.Provider.PGNIG;

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

    public void closeButtonClicked() {
        present(new UIChange<FormView>() {
            @Override
            public void change(FormView view) {
                view.hideForm();
            }
        });
    }

    public void calculateButtonClicked(String readingFrom, String readingTo,
                                       String dateFrom, String dateTo,
                                       String readingDayFrom, String readingDayTo,
                                       String readingNightFrom, String readingNightTo) {
        present(new UIChange<FormView>() {
            @Override
            public void change(FormView view) {
                view.cleanErrorsOnFields();
            }
        });

        if (provider == PGNIG || SharedPreferencesEnergyPrices.TARIFF_G11.equals(getTariff())) {
            if (isSingleReadingsFormValid(readingFrom, readingTo, dateFrom, dateTo)) {
                // TODO: save and open
            }
        } else if (isDoubleReadingsFormValid(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo)) {
            // TODO: save and open
        }
    }

    private boolean isSingleReadingsFormValid(String readingFrom, String readingTo,
                                              String dateFrom, String dateTo) {
        return isValueFilled(readingFrom, onErrorCallback(FormView.Field.READING_FROM))
                && isValueFilled(readingTo, onErrorCallback(FormView.Field.READING_TO))
                && isValueOrderCorrect(readingFrom, readingTo, onErrorCallback(FormView.Field.READING_TO))
                && isDatesOrderCorrect(dateFrom, dateTo, onDateErrorCallback());

    }

    private boolean isDoubleReadingsFormValid(String readingDayFrom, String readingDayTo,
                                              String readingNightFrom, String readingNightTo,
                                              String dateFrom, String dateTo) {
        return isValueFilled(readingDayFrom, onErrorCallback(FormView.Field.READING_DAY_FROM))
                && isValueFilled(readingDayTo, onErrorCallback(FormView.Field.READING_DAY_TO))
                && isValueFilled(readingNightFrom, onErrorCallback(FormView.Field.READING_NIGHT_FROM))
                && isValueFilled(readingNightTo, onErrorCallback(FormView.Field.READING_NIGHT_TO))
                && isValueOrderCorrect(readingDayFrom, readingDayTo, onErrorCallback(FormView.Field.READING_DAY_TO))
                && isValueOrderCorrect(readingNightFrom, readingNightTo, onErrorCallback(FormView.Field.READING_NIGHT_TO))
                && isDatesOrderCorrect(dateFrom, dateTo, onDateErrorCallback());

    }

    private FormValueValidator.OnErrorCallback onErrorCallback(final FormView.Field field) {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes final int errorMsgRes) {
                present(new UIChange<FormView>() {
                    @Override
                    public void change(FormView view) {
                        view.showReadingFieldError(field, errorMsgRes);
                    }
                });
            }
        };
    }

    private FormValueValidator.OnErrorCallback onDateErrorCallback() {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes final int errorMsgRes) {
                present(new UIChange<FormView>() {
                    @Override
                    public void change(FormView view) {
                        view.showDateFieldError(errorMsgRes);
                    }
                });
            }
        };
    }

    private void setFormValues(FormView view) {
        @SharedPreferencesEnergyPrices.TariffOption String tariff = null;
        view.setLogo(provider);
        if (provider == PGNIG) {
            view.setReadingUnit(R.string.form_reading_unit_m3);
        } else {
            tariff = getTariff();
            view.setTariffText(tariff);
            view.setReadingUnit(R.string.form_reading_unit_kWh);
        }
        setReadingsVisibility(view, tariff);
    }

    private void setReadingsVisibility(FormView view, String tariff) {
        if (provider == PGNIG) {
            view.setDoubleReadingsVisibility(View.GONE);
        } else if (SharedPreferencesEnergyPrices.TARIFF_G11.equals(tariff)) {
            view.setSingleReadingsVisibility(View.VISIBLE);
            view.setDoubleReadingsVisibility(View.GONE);
        } else {
            view.setDoubleReadingsVisibility(View.VISIBLE);
            view.setSingleReadingsVisibility(View.GONE);
        }
    }

    @SharedPreferencesEnergyPrices.TariffOption
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

        void hideForm();

        void setSingleReadingsVisibility(int visibility);

        void setDoubleReadingsVisibility(final int visibility);

        void showReadingFieldError(Field field, int errorMsgRes);

        void showDateFieldError(int errorMsgRes);

        void cleanErrorsOnFields();

        enum Field {
            READING_FROM, READING_TO,
            READING_DAY_FROM, READING_DAY_TO,
            READING_NIGHT_FROM, READING_NIGHT_TO,
        }
    }
}
