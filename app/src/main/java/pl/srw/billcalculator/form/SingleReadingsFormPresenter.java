package pl.srw.billcalculator.form;

import android.support.annotation.StringRes;

import nucleus.presenter.Presenter;
import pl.srw.billcalculator.util.Dates;

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;

/**
 * Created by kseweryn on 30.04.15.
 */
public class SingleReadingsFormPresenter extends Presenter<SingleReadingsFormView> {

    @Override
    protected void onTakeView(SingleReadingsFormView view) {
        super.onTakeView(view);
        view.initDates(Dates.format(Dates.firstDayOfThisMonth()), Dates.format(Dates.lastDayOfThisMonth()));
    }

    public void processForm(final String readingFrom, final String readingTo, String dateFrom, String dateTo) {
        if (isValueFilled(readingFrom, errorReadingCallback(0))
                && isValueFilled(readingTo, errorReadingCallback(1))
                && isValueOrderCorrect(readingFrom, readingTo, errorReadingCallback(1))
                && isDatesOrderCorrect(dateFrom, dateTo, errorDatesCallback()))
            getView().onValidationSuccess();
    }

    protected FormValueValidator.OnErrorCallback errorDatesCallback() {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes int errorMsgRes) {
                getView().onDatesError(errorMsgRes);
            }
        };
    }

    protected FormValueValidator.OnErrorCallback errorReadingCallback(final int readingIdx) {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes int errorMsgRes) {
                getView().onReadingError(readingIdx, errorMsgRes);
            }
        };
    }
}
