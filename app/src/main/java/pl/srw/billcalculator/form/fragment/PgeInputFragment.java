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
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapterFactory;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.intent.BillStoringServiceIntentFactory;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.type.Provider;

/**
* Created by Kamil Seweryn.
*/
public class PgeInputFragment extends InputFragment {

    @InjectView(R.id.ll_readings_single) LinearLayout llReadingG11;
    @InjectView(R.id.et_reading_from) AutoCompleteTextView etPreviousReading;
    @InjectView(R.id.et_reading_to) EditText etCurrentReading;

    @InjectView(R.id.tl_readings_double) TableLayout tlReadingsG12;
    @InjectView(R.id.editText_reading_day_from) AutoCompleteTextView etDayPreviousReading;
    @InjectView(R.id.editText_reading_day_to) EditText etDayCurrentReading;
    @InjectView(R.id.editText_reading_night_from) AutoCompleteTextView etNightPreviousReading;
    @InjectView(R.id.editText_reading_night_to) EditText etNightCurrentReading;

    private boolean pgeTariffG12;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_input_double, container, false);
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
    }

    private void enableAutoComplete() {
        etDayPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getActivity(), CurrentReadingType.PGE_DAY_TO));
        etNightPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getActivity(), CurrentReadingType.PGE_NIGHT_TO));
        etPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getActivity(), CurrentReadingType.PGE_TO));
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
        IntentCreator intentCreator = BillActivityIntentFactory.of(getActivity(), Provider.PGE);
        return provideExtra(intentCreator);
    }

    @Override
    protected Intent getBillStorerIntent() {
        IntentCreator intentCreator = BillStoringServiceIntentFactory.of(getActivity(), Provider.PGE);
        return provideExtra(intentCreator);
    }

    private Intent provideExtra(final IntentCreator intentCreator) {
       return null;//TODO remove
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
