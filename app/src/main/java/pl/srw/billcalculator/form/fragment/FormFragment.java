package pl.srw.billcalculator.form.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hrisey.InstanceState;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.view.DatePickingButton;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by kseweryn on 28.05.15.
 */
public class FormFragment extends Fragment {

    @InstanceState private String provider;

    @InjectView(R.id.iv_logo) ImageView ivLogo;
    @InjectView(R.id.textView_tariff_change) TextView tvTariffChange;
    @InjectView(R.id.textView_tariff) TextView tvTariff;

    @InjectView(R.id.ll_readings_single) LinearLayout llReadingG11;
    @InjectView(R.id.et_reading_from) AutoCompleteTextView etPreviousReading;
    @InjectView(R.id.et_reading_to) EditText etCurrentReading;

    @InjectView(R.id.tl_readings_double) TableLayout tlReadingsG12;
    @InjectView(R.id.editText_reading_day_from) AutoCompleteTextView etDayPreviousReading;
    @InjectView(R.id.editText_reading_day_to) EditText etDayCurrentReading;
    @InjectView(R.id.editText_reading_night_from) AutoCompleteTextView etNightPreviousReading;
    @InjectView(R.id.editText_reading_night_to) EditText etNightCurrentReading;
    
    @InjectView(R.id.button_date_from) DatePickingButton bFromDate;
    @InjectView(R.id.button_date_to) DatePickingButton bToDate;

    public static FormFragment of(Provider provider) {
        final FormFragment fragment = new FormFragment();
        fragment.provider = provider.name();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_g12, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        ivLogo.setImageResource(Provider.valueOf(provider).logoRes);

        initDates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void initDates() {
        bFromDate.setText(Dates.format(Dates.firstDayOfThisMonth()));
        bToDate.setText(Dates.format(Dates.lastDayOfThisMonth()));
    }
}
