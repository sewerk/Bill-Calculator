package pl.srw.billcalculator.form.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wnafee.vector.compat.ResourcesCompat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.FormValueValidator;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapter;
import pl.srw.billcalculator.form.view.DatePickingView;
import pl.srw.billcalculator.form.view.ReadingsEntryHandling;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.intent.BillStoringServiceIntentFactory;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.billcalculator.util.Animations;
import pl.srw.billcalculator.util.Dates;

import static pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect;
import static pl.srw.billcalculator.form.FormValueValidator.isValueFilled;
import static pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect;

/**
 * Created by kseweryn on 28.05.15.
 */
public abstract class FormFragment extends PreviousReadingsProvidingFormFragment {

    protected @Bind(R.id.toolbar) Toolbar toolbar;
    protected @Bind(R.id.iv_logo) ImageView ivLogo;
    protected @Bind(R.id.settings_link) TextView settingsLink;
    protected @Bind(R.id.tv_form_current_tariff) TextView tvTariff;

    protected @Bind(R.id.readings_single) ViewGroup vgReadingsSingle;
    protected @Bind(R.id.readings_single_errors) ViewGroup vgReadingsSingleErrors;
    protected @Bind(R.id.reading_from_layout) ReadingsEntryHandling tilPreviousReading;
    protected @Bind(R.id.reading_to_layout) ReadingsEntryHandling tilCurrentReading;

    protected @Bind(R.id.readings_double_day) ViewGroup vgReadingsDay;
    protected @Bind(R.id.readings_double_day_errors) ViewGroup vgReadingsDayErrors;
    protected @Bind(R.id.readings_double_night) ViewGroup vgReadingsNight;
    protected @Bind(R.id.readings_double_night_errors) ViewGroup vgReadingsNightErrors;

    protected @Bind(R.id.reading_day_from_layout) ReadingsEntryHandling tilDayPreviousReading;
    protected @Bind(R.id.reading_day_to_layout) ReadingsEntryHandling tilDayCurrentReading;
    protected @Bind(R.id.reading_night_from_layout) ReadingsEntryHandling tilNightPreviousReading;
    protected @Bind(R.id.reading_night_to_layout) ReadingsEntryHandling tilNightCurrentReading;

    protected @Bind(R.id.button_date_from)
    DatePickingView bFromDate;
    protected @Bind(R.id.button_date_to)
    DatePickingView bToDate;
    protected @Bind(R.id.button_calculate) FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setupToolbar();
        setupForm();
        cachePreviousReadings();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateReadings();
    }

    private void setupForm() {
        ivLogo.setImageResource(getProvider().logoRes);
        setupSettingsLink();
        setupReadings();
        setupDates();
        Drawable arrow = ResourcesCompat.getDrawable(getActivity(), R.drawable.ic_arrow_forward_white_24px);
        fab.setImageDrawable(arrow);
    }

    private void setupReadings() {
        final String previous = getString(R.string.form_reading_from_kWh_hint);
        tilPreviousReading.setHint(previous);
        tilDayPreviousReading.setHint(previous);
        tilNightPreviousReading.setHint(previous);
        final String current = getString(R.string.form_reading_to_kWh_hint);
        tilCurrentReading.setHint(current);
        tilDayCurrentReading.setHint(current);
        tilNightCurrentReading.setHint(current);
    }

    private void updateReadings() {
        final boolean tariffG12 = isTariffG12();
        if (tariffG12 && "G12".equals(tvTariff.getText().toString())
                || "G11".equals(tvTariff.getText().toString()))
            return;

        if (tariffG12) {
            tvTariff.setText("G12");
            vgReadingsSingle.setVisibility(View.GONE);
            vgReadingsSingleErrors.setVisibility(View.GONE);
            vgReadingsDay.setVisibility(View.VISIBLE);
            vgReadingsDayErrors.setVisibility(View.VISIBLE);
            vgReadingsNight.setVisibility(View.VISIBLE);
            vgReadingsNightErrors.setVisibility(View.VISIBLE);
        } else {
            tvTariff.setText("G11");
            vgReadingsSingle.setVisibility(View.VISIBLE);
            vgReadingsSingleErrors.setVisibility(View.VISIBLE);
            vgReadingsDay.setVisibility(View.GONE);
            vgReadingsDayErrors.setVisibility(View.GONE);
            vgReadingsNight.setVisibility(View.GONE);
            vgReadingsNightErrors.setVisibility(View.GONE);
        }
    }

    protected abstract boolean isTariffG12();

    private void setupSettingsLink() {
        final String settingsLabel = getString(R.string.settings_label);
        final SpannableString span = new SpannableString(settingsLabel);
        span.setSpan(new UnderlineSpan(), 0, settingsLabel.length(), 0);
        settingsLink.setText(span);
        settingsLink.setMovementMethod(new LinkMovementMethod());
    }

    @OnClick(R.id.settings_link)
    public void onSettingsLinkClicked(View widget) {
        final Context context = widget.getContext();
        context.startActivity(ProviderSettingsActivity.createIntent(context, getProvider()));
    }

    private void setupToolbar() {
        toolbar.setTitle(getTitle());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract int getTitle();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void setPreviousReadings() {
        final CurrentReadingType[] currentReadingTypes = getCurrentReadingTypes();

        ((AutoCompleteTextView) tilPreviousReading.getEditText())
                .setAdapter(new PreviousReadingsAdapter(getActivity(), getPreviousReadings(currentReadingTypes[0])));

        if (currentReadingTypes.length > 1) {
            ((AutoCompleteTextView) tilDayPreviousReading.getEditText())
                    .setAdapter(new PreviousReadingsAdapter(getActivity(), getPreviousReadings(currentReadingTypes[1])));
            ((AutoCompleteTextView) tilNightPreviousReading.getEditText())
                    .setAdapter(new PreviousReadingsAdapter(getActivity(), getPreviousReadings(currentReadingTypes[2])));
        }
    }

    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        if (isTariffG12()) {
            String readingDayFrom = tilDayPreviousReading.getText();
            String readingDayTo = tilDayCurrentReading.getText();
            String readingNightFrom = tilNightPreviousReading.getText();
            String readingNightTo = tilNightCurrentReading.getText();
            if (isValueFilled(readingDayFrom, errorReadingCallback(tilDayPreviousReading))
                    && isValueFilled(readingDayTo, errorReadingCallback(tilDayCurrentReading))
                    && isValueFilled(readingNightFrom, errorReadingCallback(tilNightPreviousReading))
                    && isValueFilled(readingNightTo, errorReadingCallback(tilNightCurrentReading))
                    && isValueOrderCorrect(readingDayFrom, readingDayTo, errorReadingCallback(tilDayCurrentReading))
                    && isValueOrderCorrect(readingNightFrom, readingNightTo, errorReadingCallback(tilNightCurrentReading))
                    && isDatesOrderCorrect(bFromDate.getText().toString(), bToDate.getText().toString(), errorDatesCallback()))
                this.onValidationSuccess();
        } else {
            final String readingFrom = tilPreviousReading.getText();
            final String readingTo = tilCurrentReading.getText();
            if (isValueFilled(readingFrom, errorReadingCallback(tilPreviousReading))
                    && isValueFilled(readingTo, errorReadingCallback(tilCurrentReading))
                    && isValueOrderCorrect(readingFrom, readingTo, errorReadingCallback(tilCurrentReading))
                    && isDatesOrderCorrect(bFromDate.getText().toString(), bToDate.getText().toString(), errorDatesCallback()))
                this.onValidationSuccess();
        }

    }

    protected Intent provideExtra(final IntentCreator intentCreator) {
        if (isTariffG12())
            return intentCreator.from(tilDayPreviousReading.getEditText(), tilDayCurrentReading.getEditText(),
                    tilNightPreviousReading.getEditText(), tilNightCurrentReading.getEditText(),
                    bFromDate, bToDate);
        else
            return intentCreator.from(tilPreviousReading.getEditText(), tilCurrentReading.getEditText(), bFromDate, bToDate);
    }

    private Intent getBillActivityIntent() {
        IntentCreator intentCreator = BillActivityIntentFactory.of(getActivity(), getProvider());
        return provideExtra(intentCreator);
    }

    private Intent getBillStorerIntent() {
        IntentCreator intentCreator = BillStoringServiceIntentFactory.of(getActivity(), getProvider());
        return provideExtra(intentCreator);
    }

    protected void onValidationSuccess() {
        getActivity().startActivity(getBillActivityIntent());
        getActivity().startService(getBillStorerIntent());
    }

    protected FormValueValidator.OnErrorCallback errorReadingCallback(final ReadingsEntryHandling etView) {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes int errorMsgRes) {
                FormFragment.this.onError(etView, errorMsgRes);
            }
        };
    }

    protected FormValueValidator.OnErrorCallback errorDatesCallback() {
        return new FormValueValidator.OnErrorCallback() {
            @Override
            public void onError(@StringRes int errorMsgRes) {
                FormFragment.this.onDatesError(errorMsgRes);
            }
        };
    }

    private void onError(ReadingsEntryHandling layout, @StringRes int errorMsgRes) {
        final EditText et = layout.getEditText();
        Animations.shake(et);
        layout.setError(errorMsgRes);
        et.requestFocus();
        showKeyboard(et);
    }

    private void onDatesError(@StringRes int errorMsgRes) {
        Animations.shake(bToDate);
        bToDate.setError(getActivity().getString(errorMsgRes));
    }

    private void setupDates() {
        bFromDate.setText(Dates.format(Dates.firstDayOfThisMonth()));
        bToDate.setText(Dates.format(Dates.lastDayOfThisMonth()));
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }
}


