package pl.srw.billcalculator.form.fragment;

import pl.srw.billcalculator.form.FormValueValidator;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.ProviderMapper;
import pl.srw.billcalculator.wrapper.Analytics;

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;
import static pl.srw.billcalculator.type.Provider.PGNIG;

public class FormPresenter {

    private final FormPresenter.FormView view;
    private final Provider provider;
    private final ProviderMapper providerMapper;

    private final HistoryChangeListener historyUpdater;

    public FormPresenter(FormView view, Provider provider, ProviderMapper providerMapper, HistoryChangeListener historyUpdater) {
        this.view = view;
        this.provider = provider;
        this.providerMapper = providerMapper;
        this.historyUpdater = historyUpdater;
    }

    public void closeButtonClicked() {
        view.hideForm();
    }

    public void calculateButtonClicked(String readingFrom, String readingTo,
                                       String dateFrom, String dateTo,
                                       String readingDayFrom, String readingDayTo,
                                       String readingNightFrom, String readingNightTo) {
        view.cleanErrorsOnFields();

        if (provider == PGNIG || SharedPreferencesEnergyPrices.TARIFF_G11.equals(getTariff())) {
            if (!isSingleReadingsFormValid(readingFrom, readingTo, dateFrom, dateTo)) {
                return;
            }
            view.startStoringServiceForSingleReadings(provider);
            view.startBillActivityForSingleReadings(provider);
            view.hideForm();
        } else {
            if (!isDoubleReadingsFormValid(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo)) {
                return;
            }
            view.startStoringServiceForDoubleReadings(provider);
            view.startBillActivityForDoubleReadings(provider);
            view.hideForm();
        }
        historyUpdater.onHistoryChanged();
        Analytics.logAction(ActionType.CALCULATE, "provider", provider);
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
        return errorMsgRes -> view.showReadingFieldError(field, errorMsgRes);
    }

    private FormValueValidator.OnErrorCallback onDateErrorCallback() {
        return errorMsgRes -> view.showDateFieldError(errorMsgRes);
    }

    @SharedPreferencesEnergyPrices.TariffOption
    private String getTariff() {
        return ((SharedPreferencesEnergyPrices)providerMapper.getPrices(provider)).getTariff();
    }

    public interface FormView {

        void hideForm();

        void showReadingFieldError(Field field, int errorMsgRes);

        void showDateFieldError(int errorMsgRes);

        void cleanErrorsOnFields();

        void startStoringServiceForSingleReadings(Provider provider);

        void startBillActivityForSingleReadings(Provider provider);

        void startStoringServiceForDoubleReadings(Provider provider);

        void startBillActivityForDoubleReadings(Provider provider);

        enum Field {
            READING_FROM, READING_TO,
            READING_DAY_FROM, READING_DAY_TO,
            READING_NIGHT_FROM, READING_NIGHT_TO,
        }
    }

    public interface HistoryChangeListener {
        void onHistoryChanged();
    }
}
