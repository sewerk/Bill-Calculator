package pl.srw.billcalculator.form.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;

/**
 * Created by kseweryn on 29.05.15.
 */
public abstract class DoubleReadingsFormFragment extends SingleReadingFormFragment {

    protected @InjectView(R.id.textView_tariff_change) TextView tvTariffChange;
    protected @InjectView(R.id.textView_tariff) TextView tvTariff;

    protected @InjectView(R.id.tl_readings_double) TableLayout tlReadingsG12;
    protected @InjectView(R.id.editText_reading_day_from) AutoCompleteTextView etDayPreviousReading;
    protected @InjectView(R.id.editText_reading_day_to) EditText etDayCurrentReading;
    protected @InjectView(R.id.editText_reading_night_from) AutoCompleteTextView etNightPreviousReading;
    protected @InjectView(R.id.editText_reading_night_to) EditText etNightCurrentReading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_g12, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTariffChange.setText(Html.fromHtml(getString(R.string.tariff_change)));
    }

    @Override
    public void onStart() {
        super.onStart();
        setTariffLabel();
        showTariffDependentReadings();
    }

    protected abstract boolean isTariffG12();

    @OnClick(R.id.textView_tariff_change)
    public void moveToChangeTariff() {
        final Intent intent = ProviderSettingsActivity
                .createIntent(getActivity(), getProvider());
        startActivity(intent);
    }

    @Override
    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        if (isTariffG12()) {
            String readingDayFrom = etDayPreviousReading.getText().toString();
            String readingDayTo = etDayCurrentReading.getText().toString();
            String readingNightFrom = etNightPreviousReading.getText().toString();
            String readingNightTo = etNightCurrentReading.getText().toString();
            if (isValueFilled(readingDayFrom, errorReadingCallback(etDayPreviousReading))
                    && isValueFilled(readingDayTo, errorReadingCallback(etDayCurrentReading))
                    && isValueFilled(readingNightFrom, errorReadingCallback(etNightPreviousReading))
                    && isValueFilled(readingNightTo, errorReadingCallback(etNightCurrentReading))
                    && isValueOrderCorrect(readingDayFrom, readingDayTo, errorReadingCallback(etDayCurrentReading))
                    && isValueOrderCorrect(readingNightFrom, readingNightTo, errorReadingCallback(etNightCurrentReading))
                    && isDatesOrderCorrect(bFromDate.getText().toString(), bToDate.getText().toString(), errorDatesCallback()))
                this.onValidationSuccess();
        } else
            super.calculate();
    }

    @Override
    protected Intent provideExtra(final IntentCreator intentCreator) {
        if (isTariffG12())
            return intentCreator.from(etDayPreviousReading, etDayCurrentReading,
                    etNightPreviousReading, etNightCurrentReading,
                    bFromDate, bToDate);
        else
            return super.provideExtra(intentCreator);
    }

    private void setTariffLabel() {
        if (isTariffG12()) setTariffLabel(R.string.tariff_G12_on_bill);
        else setTariffLabel(R.string.tariff_G11_on_bill);
    }

    private void showTariffDependentReadings() {
        if (isTariffG12()) showDoubleReadings();
        else showSingleReadings();
    }

    private void setTariffLabel(@StringRes final int tariffTextId) {
        tvTariff.setText(tariffTextId);
    }

    private void showSingleReadings() {
        llReadingG11.setVisibility(View.VISIBLE);
        tlReadingsG12.setVisibility(View.GONE);
    }

    private void showDoubleReadings() {
        llReadingG11.setVisibility(View.GONE);
        tlReadingsG12.setVisibility(View.VISIBLE);
    }
}
