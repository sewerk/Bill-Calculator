package pl.srw.billcalculator.form;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 27.05.15.
 */
public abstract class DoubleReadingsFormView extends SingleReadingsFormView<DoubleReadingsFormPresenter> {

    protected @InjectView(R.id.textView_tariff_change) TextView tvTariffChange;
    protected @InjectView(R.id.textView_tariff) TextView tvTariff;

    protected @InjectView(R.id.tl_readings_double) TableLayout tlReadingsG12;
    protected @InjectView(R.id.editText_reading_day_from) AutoCompleteTextView etDayPreviousReading;
    protected @InjectView(R.id.editText_reading_day_to) EditText etDayCurrentReading;
    protected @InjectView(R.id.editText_reading_night_from) AutoCompleteTextView etNightPreviousReading;
    protected @InjectView(R.id.editText_reading_night_to) EditText etNightCurrentReading;

    public DoubleReadingsFormView(Context context) {
        super(context);
    }

    public DoubleReadingsFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        inflate(getContext(), R.layout.form_g12, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvTariffChange.setText(Html.fromHtml(getContext().getString(R.string.tariff_change)));
    }

    @Override
    protected abstract Provider getProvider();

    public void showSingleReadings() {
        llReadingG11.setVisibility(View.VISIBLE);
        tlReadingsG12.setVisibility(View.GONE);
    }

    public void showDoubleReadings() {
        llReadingG11.setVisibility(View.GONE);
        tlReadingsG12.setVisibility(View.VISIBLE);
    }

    public void setTariffLabel(@StringRes final int tariffTextId) {
        tvTariff.setText(tariffTextId);
    }

    @OnClick(R.id.textView_tariff_change)
    public void moveToChangeTariff() {
        final Intent intent = ProviderSettingsActivity
                .createIntent(getContext(), getProvider());
        getContext().startActivity(intent);
    }

    @Override
    protected abstract void attachPreviousReadingAdapter();

    @Override
    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        if (getPresenter().isTariffG12())
            getPresenter().processForm(etDayPreviousReading.getText().toString(), etDayCurrentReading.getText().toString(),
                    etNightPreviousReading.getText().toString(), etNightCurrentReading.getText().toString(),
                    bFromDate.getText().toString(), bToDate.getText().toString());
        else
            super.calculate();
    }

    protected Intent provideExtra(final IntentCreator intentCreator) {
        if (getPresenter().isTariffG12())
            return intentCreator.from(etDayPreviousReading, etDayCurrentReading,
                    etNightPreviousReading, etNightCurrentReading,
                    bFromDate, bToDate);
        else
            return super.provideExtra(intentCreator);
    }

    protected EditText getView(final int readingIdx) {
        if (getPresenter().isTariffG12()) {
            switch (readingIdx) {
                case 0:
                    return etDayPreviousReading;
                case 1:
                    return etDayCurrentReading;
                case 2:
                    return etNightPreviousReading;
                case 3:
                    return etNightCurrentReading;
            }
        }
        return super.getView(readingIdx);
    }
}
