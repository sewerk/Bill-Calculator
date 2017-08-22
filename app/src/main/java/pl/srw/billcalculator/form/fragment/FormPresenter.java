package pl.srw.billcalculator.form.fragment;

import android.support.annotation.StringRes;
import android.view.View;

import org.threeten.bp.LocalDate;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pl.srw.billcalculator.form.FormValueValidator;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.ProviderMapper;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.billcalculator.wrapper.ReadingsRepo;
import pl.srw.mfvp.MvpPresenter;
import pl.srw.mfvp.di.scope.RetainFragmentScope;

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;
import static pl.srw.billcalculator.type.Provider.PGNIG;

@RetainFragmentScope
public class FormPresenter extends MvpPresenter<FormPresenter.FormView> {

    private final ReadingsRepo readingsRepo;
    private final ProviderMapper providerMapper;
    private final HistoryUpdating historyUpdater;

    private Provider provider;

    @Inject
    public FormPresenter(ReadingsRepo readingsRepo, ProviderMapper providerMapper, HistoryUpdating historyUpdater) {
        this.readingsRepo = readingsRepo;
        this.providerMapper = providerMapper;
        this.historyUpdater = historyUpdater;
    }

    public void setup(Provider provider) {
        this.provider = provider;
    }

    @Override
    protected void onFirstBind() {
        present(view -> {
            setFormValues(view);
            view.setDates(Dates.firstDayOfThisMonth(), Dates.lastDayOfThisMonth());
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        present(this::setFormValues);
    }

    public void settingsLinkClicked() {
        present(view -> view.showProviderSettings(provider));
    }

    public void closeButtonClicked() {
        present(FormView::hideForm);
    }

    public void calculateButtonClicked(String readingFrom, String readingTo,
                                       String dateFrom, String dateTo,
                                       String readingDayFrom, String readingDayTo,
                                       String readingNightFrom, String readingNightTo) {
        present(FormView::cleanErrorsOnFields);

        if (provider == PGNIG || SharedPreferencesEnergyPrices.TARIFF_G11.equals(getTariff())) {
            if (!isSingleReadingsFormValid(readingFrom, readingTo, dateFrom, dateTo)) {
                return;
            }
            present(view -> {
                view.startStoringServiceForSingleReadings(provider);
                view.startBillActivityForSingleReadings(provider);
                view.hideForm();
            });
        } else {
            if (!isDoubleReadingsFormValid(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo)) {
                return;
            }
            present(view -> {
                view.startStoringServiceForDoubleReadings(provider);
                view.startBillActivityForDoubleReadings(provider);
                view.hideForm();
            });
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
        return errorMsgRes -> present(view -> view.showReadingFieldError(field, errorMsgRes));
    }

    private FormValueValidator.OnErrorCallback onDateErrorCallback() {
        return errorMsgRes -> present(view -> view.showDateFieldError(errorMsgRes));
    }

    private void setFormValues(FormView view) {
        view.setupSettingsLink();
        view.setLogo(provider);
        view.setReadingUnit(provider.formReadingUnit);
        if (provider == PGNIG) {
            view.setDoubleReadingsVisibility(View.GONE);
            getPreviousReadings(CurrentReadingType.PGNIG_TO,
                    readings -> present(formView -> formView.setAutoCompleteDataForReadingFrom(readings)));
        } else {
            String tariff = getTariff();
            if (SharedPreferencesEnergyPrices.TARIFF_G11.equals(tariff)) {
                view.setSingleReadingsVisibility(View.VISIBLE);
                view.setDoubleReadingsVisibility(View.GONE);
                getPreviousReadings(provider.singleReadingType,
                        readings -> present(formView -> formView.setAutoCompleteDataForReadingFrom(readings)));
            } else {
                view.setDoubleReadingsVisibility(View.VISIBLE);
                view.setSingleReadingsVisibility(View.GONE);
                getPreviousReadings(provider.doubleReadingTypes[0],
                        readings -> present(formView -> formView.setAutoCompleteDataForReadingDayFrom(readings)));
                getPreviousReadings(provider.doubleReadingTypes[1],
                        readings -> present(formView -> formView.setAutoCompleteDataForReadingNightFrom(readings)));
            }
            view.setTariffText(tariff);
        }
    }

    private void getPreviousReadings(CurrentReadingType type, Consumer<int[]> onSuccess) {
        readingsRepo.getPreviousReadingsFor(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess);
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

        void setDates(LocalDate fromDate, LocalDate toDate);

        void setAutoCompleteDataForReadingFrom(int[] readings);

        void setAutoCompleteDataForReadingDayFrom(int[] readings);

        void setAutoCompleteDataForReadingNightFrom(int[] readings);

        void setReadingUnit(@StringRes int unitResId);

        void hideForm();

        void setSingleReadingsVisibility(int visibility);

        void setDoubleReadingsVisibility(final int visibility);

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

    public interface HistoryUpdating {
        void onHistoryChanged();
    }
}
