package pl.srw.billcalculator.form.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.FormValueValidator;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapter;
import pl.srw.billcalculator.form.view.DatePickingButton;
import pl.srw.billcalculator.form.view.ErrorShowingDatePickerButton;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.intent.BillStoringServiceIntentFactory;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.util.Animations;
import pl.srw.billcalculator.util.Dates;

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;

/**
 * Created by kseweryn on 28.05.15.
 */
public abstract class SingleReadingsFormFragment extends PreviousReadingsProvidingFormFragment {

    protected @InjectView(R.id.iv_logo) ImageView ivLogo;
    
    protected @InjectView(R.id.ll_readings_single) LinearLayout llReadingG11;
    protected @InjectView(R.id.et_reading_from) AutoCompleteTextView etPreviousReading;
    protected @InjectView(R.id.et_reading_to) EditText etCurrentReading;

    protected @InjectView(R.id.button_date_from) DatePickingButton bFromDate;
    protected @InjectView(R.id.button_date_to) ErrorShowingDatePickerButton bToDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_g11, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        ivLogo.setImageResource(getProvider().logoRes);
        cachePreviousReadings();
        initDates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    protected void setPreviousReadings() {
        etPreviousReading.setAdapter(new PreviousReadingsAdapter(getActivity(), getPreviousReadings(getCurrentReadingTypes()[0])));
    }

    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        final String readingFrom = etPreviousReading.getText().toString();
        final String readingTo = etCurrentReading.getText().toString();
        if (isValueFilled(readingFrom, errorReadingCallback(etPreviousReading))
                && isValueFilled(readingTo, errorReadingCallback(etCurrentReading))
                && isValueOrderCorrect(readingFrom, readingTo, errorReadingCallback(etCurrentReading))
                && isDatesOrderCorrect(bFromDate.getText().toString(), bToDate.getText().toString(), errorDatesCallback()))
            this.onValidationSuccess();
    }

    protected Intent getBillActivityIntent() {
        IntentCreator intentCreator = BillActivityIntentFactory.of(getActivity(), getProvider());
        return provideExtra(intentCreator);
    }

    protected Intent getBillStorerIntent() {
        IntentCreator intentCreator = BillStoringServiceIntentFactory.of(getActivity(), getProvider());
        return provideExtra(intentCreator);
    }

    protected Intent provideExtra(final IntentCreator intentCreator) {
        return intentCreator.from(etPreviousReading, etCurrentReading, bFromDate, bToDate);
    }

    protected void onValidationSuccess() {
        getActivity().startActivity(getBillActivityIntent());
        getActivity().startService(getBillStorerIntent());
    }

    protected FormValueValidator.OnErrorCallback errorReadingCallback(final EditText etView) {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes int errorMsgRes) {
                SingleReadingsFormFragment.this.onError(etView, errorMsgRes);
            }
        };
    }

    protected FormValueValidator.OnErrorCallback errorDatesCallback() {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes int errorMsgRes) {
                SingleReadingsFormFragment.this.onDatesError(errorMsgRes);
            }
        };
    }

    private void onError(EditText et, @StringRes int errorMsgRes) {
        Animations.shake(et);
        et.setError(getActivity().getString(errorMsgRes));
        et.requestFocus();
        showKeyboard(et);
    }

    private void onDatesError(@StringRes int errorMsg) {
        Animations.shake(bToDate);
        bToDate.showError(errorMsg);
    }

    private void initDates() {
        bFromDate.setText(Dates.format(Dates.firstDayOfThisMonth()));
        bToDate.setText(Dates.format(Dates.lastDayOfThisMonth()));
        bFromDate.setOnDatePickedListener(new DatePickingButton.OnDatePickedListener() {
            @Override
            public void onDatePicked(DatePickingButton view) {
                bToDate.clearError();
            }
        });
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }
}
