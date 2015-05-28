package pl.srw.billcalculator.form;

import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.event.TariffChangedEvent;

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;

/**
 * Created by kseweryn on 27.05.15.
 */
public abstract class DoubleReadingsFormPresenter extends SingleReadingsFormPresenter {
    protected DoubleReadingsFormView view;

    @Override
    protected void onTakeView(SingleReadingsFormView _view) {
        this.view = (DoubleReadingsFormView) _view;
        super.onTakeView(view);
        changeTariffDependentViews();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDropView() {
        super.onDropView();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(TariffChangedEvent event) {
        if (event.getProvider() == getView().getProvider())
            changeTariffDependentViews();
    }

    private void changeTariffDependentViews() {
        if (isTariffG12()) {
            view.showDoubleReadings();
            view.setTariffLabel(R.string.pge_tariff_G12_on_bill);
        } else {
            view.showSingleReadings();
            view.setTariffLabel(R.string.pge_tariff_G11_on_bill);
        }
    }

    public void processForm(String readingDayFrom, String readingDayTo, String readingNightFrom, String readingNightTo, String dateFrom, String dateTo) {
        if (isValueFilled(readingDayFrom, errorReadingCallback(0))
                && isValueFilled(readingDayTo, errorReadingCallback(1))
                && isValueFilled(readingNightFrom, errorReadingCallback(2))
                && isValueFilled(readingNightTo, errorReadingCallback(3))
                && isValueOrderCorrect(readingDayFrom, readingDayTo, errorReadingCallback(1))
                && isValueOrderCorrect(readingNightFrom, readingNightTo, errorReadingCallback(3))
                && isDatesOrderCorrect(dateFrom, dateTo, errorDatesCallback()))
            getView().onValidationSuccess();
    }

    public abstract boolean isTariffG12();
}
