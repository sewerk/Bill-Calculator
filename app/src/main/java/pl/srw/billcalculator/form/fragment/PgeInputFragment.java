package pl.srw.billcalculator.form.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import butterknife.InjectView;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.adapter.PreviousReadingsAdapter;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.intent.BillStoringServiceIntentFactory;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.preference.GeneralPreferences;
import pl.srw.billcalculator.type.BillType;

/**
* Created by Kamil Seweryn.
*/
public class PgeInputFragment extends InputFragment {

    @InjectView(R.id.linearLayout_readings) LinearLayout llReadingG11;
    @InjectView(R.id.et_reading_from) AutoCompleteTextView etPreviousReading;
    @InjectView(R.id.et_reading_to) EditText etCurrentReading;

    @InjectView(R.id.tableLayout_G12_readings) TableLayout tlReadingsG12;
    @InjectView(R.id.editText_reading_day_from) AutoCompleteTextView etDayPreviousReading;
    @InjectView(R.id.editText_reading_day_to) EditText etDayCurrentReading;
    @InjectView(R.id.editText_reading_night_from) AutoCompleteTextView etNightPreviousReading;
    @InjectView(R.id.editText_reading_night_to) EditText etNightCurrentReading;

    private PreviousReadingsAdapter dayReadingAdapter;
    private PreviousReadingsAdapter nightReadingAdapter;
    private PreviousReadingsAdapter readingAdapter;
    private boolean pgeTariffG12;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_pge_input, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableAutoComplete();
    }

    @Override
    public void onStart() {
        super.onStart();
        pgeTariffG12 = GeneralPreferences.isPgeTariffG12();
        chooseReadings();
        updateAutoComplete();
    }

    private void enableAutoComplete() {
        etDayPreviousReading.setAdapter(getDayReadingAdapter());
        etNightPreviousReading.setAdapter(getNightReadingAdapter());
        etPreviousReading.setAdapter(getReadingAdapter());
    }

    private PreviousReadingsAdapter getReadingAdapter() {
        if (readingAdapter == null) {
            readingAdapter = new PreviousReadingsAdapter(getActivity(), CurrentReadingType.PGE_TO);
        }
        return readingAdapter;
    }

    private PreviousReadingsAdapter getNightReadingAdapter() {
        if (nightReadingAdapter == null) {
            nightReadingAdapter = new PreviousReadingsAdapter(getActivity(), CurrentReadingType.PGE_NIGHT_TO);
        }
        return nightReadingAdapter;
    }

    private PreviousReadingsAdapter getDayReadingAdapter() {
        if (dayReadingAdapter == null) {
            dayReadingAdapter = new PreviousReadingsAdapter(getActivity(), CurrentReadingType.PGE_DAY_TO);
        }
        return dayReadingAdapter;
    }

    @DebugLog
    private void updateAutoComplete() {
        if (pgeTariffG12) {
            getDayReadingAdapter().updateAll();
            getNightReadingAdapter().updateAll();
        } else
            getReadingAdapter().updateAll();
    }

    @DebugLog
    protected void markHistoryChanged() {
        markHistoryChangedFor(etDayPreviousReading);
        markHistoryChangedFor(etNightPreviousReading);
        markHistoryChangedFor(etPreviousReading);
    }

    private void markHistoryChangedFor(final AutoCompleteTextView et) {
        ((PreviousReadingsAdapter)et.getAdapter()).notifyInputDataChanged();
    }

    private void chooseReadings() {
        if (pgeTariffG12) {
            llReadingG11.setVisibility(View.GONE);
            tlReadingsG12.setVisibility(View.VISIBLE);
        } else {
            llReadingG11.setVisibility(View.VISIBLE);
            tlReadingsG12.setVisibility(View.GONE);
        }
    }

    @Override
    protected Intent getBillActivityIntent() {
        IntentCreator intentCreator = BillActivityIntentFactory.of(getActivity(), BillType.PGE);
        return provideExtra(intentCreator);
    }

    @Override
    protected Intent getBillStorerIntent() {
        IntentCreator intentCreator = BillStoringServiceIntentFactory.of(getActivity(), BillType.PGE);
        return provideExtra(intentCreator);
    }

    private Intent provideExtra(final IntentCreator intentCreator) {
        if (pgeTariffG12)
            return intentCreator.from(etDayPreviousReading, etDayCurrentReading,
                    etNightPreviousReading, etNightCurrentReading,
                    bFromDate, bToDate);
        else
            return intentCreator.from(etPreviousReading, etCurrentReading, bFromDate, bToDate);
    }

    @Override
    protected boolean validateReadings() {
        if (pgeTariffG12) {
            return validateReadingsG12();
        } else {
            return validateReadingsG11();
        }
    }

    private boolean validateReadingsG11() {
        return validateMissingValue(etPreviousReading) && validateMissingValue(etCurrentReading)
                && validateValueOrder(etPreviousReading, etCurrentReading);
    }

    private boolean validateReadingsG12() {
        return validateMissingValue(etDayPreviousReading) && validateMissingValue(etDayCurrentReading)
                && validateMissingValue(etNightPreviousReading) && validateMissingValue(etNightCurrentReading)
                && validateValueOrder(etDayPreviousReading, etDayCurrentReading) && validateValueOrder(etNightPreviousReading, etNightCurrentReading);
    }
}
