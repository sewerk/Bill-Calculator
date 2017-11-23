package pl.srw.billcalculator.form.fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.threeten.bp.LocalDate;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.FormVM;
import pl.srw.billcalculator.form.FormVMFactory;
import pl.srw.billcalculator.form.autocomplete.PreviousReadingsAdapter;
import pl.srw.billcalculator.form.di.FormComponent;
import pl.srw.billcalculator.form.view.DatePickingView;
import pl.srw.billcalculator.form.view.InstantAutoCompleteTextInputEditText;
import pl.srw.billcalculator.form.view.RoundedLogoView;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.intent.BillStoringServiceIntentFactory;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Animations;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Views;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.mfvp.MvpFragment;
import pl.srw.mfvp.di.MvpFragmentScopedFragment;

public class FormFragment extends MvpFragment
        implements MvpFragmentScopedFragment<FormComponent, HistoryComponent>,
        FormPresenter.FormView {

    public static final String DATE_PATTERN = "dd.MM.yy";
    private static final String EXTRA_PROVIDER = "PROVIDER";

    @Inject
    FormPresenter presenter;

    @Inject
    FormVMFactory formVMFactory;

    @BindView(R.id.form_logo) RoundedLogoView logoView;
    @BindView(R.id.form_settings_link) TextView settingsLink;
    @BindView(R.id.form_entry_tariff) TextView tariffView;
    @BindView(R.id.form_entry_reading_unit) TextView unitView;
    @BindViews({R.id.form_entry_reading_icon, R.id.form_entry_reading_from, R.id.form_entry_reading_separator,
            R.id.form_entry_reading_to, R.id.form_entry_reading_unit}) View[] singleReadingsGroup;
    @BindViews({R.id.form_entry_reading_day_icon, R.id.form_entry_reading_day_from, R.id.form_entry_reading_day_separator,
            R.id.form_entry_reading_day_to, R.id.form_entry_reading_day_unit, R.id.form_entry_reading_night_icon,
            R.id.form_entry_reading_night_from, R.id.form_entry_reading_night_separator,
            R.id.form_entry_reading_night_to, R.id.form_entry_reading_night_unit}) View[] doubleReadingsGroups;
    @BindView(R.id.form_entry_dates_from) DatePickingView dateFromView;
    @BindView(R.id.form_entry_dates_to) DatePickingView dateToView;

    @BindView(R.id.form_entry_reading_from) TextInputLayout readingFrom;
    @BindView(R.id.form_entry_reading_from_input) InstantAutoCompleteTextInputEditText readingFromInput;
    @BindView(R.id.form_entry_reading_to) TextInputLayout readingTo;
    @BindView(R.id.form_entry_reading_day_from) TextInputLayout readingDayFrom;
    @BindView(R.id.form_entry_reading_day_from_input) InstantAutoCompleteTextInputEditText readingDayFromInput;
    @BindView(R.id.form_entry_reading_day_to) TextInputLayout readingDayTo;
    @BindView(R.id.form_entry_reading_night_from) TextInputLayout readingNightFrom;
    @BindView(R.id.form_entry_reading_night_from_input) InstantAutoCompleteTextInputEditText readingNightFromInput;
    @BindView(R.id.form_entry_reading_night_to) TextInputLayout readingNightTo;

    private FormVM formVM;
    private Unbinder unbinder;

    private final DialogInterface.OnKeyListener onBackListener = (dialog, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            Analytics.log("Form: back pressed");
        }
        return false;
    };

    public static FormFragment newInstance(Provider provider) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_PROVIDER, provider.ordinal());
        FormFragment fragment = new FormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int providerIdx = getArguments().getInt(EXTRA_PROVIDER);
        final Provider provider = Provider.values()[providerIdx];
        presenter.setup(provider);
        attachPresenter(presenter);

        formVM = ViewModelProviders.of(this, formVMFactory).get(FormVM.class);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int providerIdx = getArguments().getInt(EXTRA_PROVIDER);
        final Provider provider = Provider.values()[providerIdx];
        formVM.init(provider);

        setDates(formVM.getFromDate(), formVM.getToDate());
        formVM.getSinglePrevReadings().observe(this, readings -> setReadingsForAutocomplete(readingFromInput, readings));
        formVM.getDayPrevReadings().observe(this, readings -> setReadingsForAutocomplete(readingDayFromInput, readings));
        formVM.getNightPrevReadings().observe(this, readings -> setReadingsForAutocomplete(readingNightFromInput, readings));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.form, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(onBackListener);

        unbinder = ButterKnife.bind(this, view);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public FormComponent prepareComponent(HistoryComponent historyComponent) {
        return historyComponent.getFormComponent();
    }

    @Override
    public void setupSettingsLink() {
        final String settingsLabel = settingsLink.getText().toString();
        final SpannableString span = new SpannableString(settingsLabel);
        span.setSpan(new UnderlineSpan(), 0, settingsLabel.length(), 0);
        settingsLink.setText(span);
        settingsLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnTextChanged(value = R.id.form_entry_dates_from, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onDateFromChanged(CharSequence text) {
        formVM.setFromDate(Dates.parse(text, DATE_PATTERN));
    }

    @OnTextChanged(value = R.id.form_entry_dates_to, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onDateToChanged(CharSequence text) {
        formVM.setToDate(Dates.parse(text, DATE_PATTERN));
    }

    @OnClick(R.id.form_settings_link)
    void onSettingsLinkClicked() {
        Analytics.log("Form: Settings link clicked");
        presenter.settingsLinkClicked();
    }

    @OnClick(R.id.close_button)
    void closeButtonClicked() {
        Analytics.log("Form: Close clicked");
        presenter.closeButtonClicked();
    }

    @OnClick(R.id.calculate_button)
    void calculateButtonClicked() {
        Analytics.log("Form: Calculate clicked");
        presenter.calculateButtonClicked(Views.getText(readingFrom), Views.getText(readingTo),
                Views.getText(dateFromView), Views.getText(dateToView),
                Views.getText(readingDayFrom), Views.getText(readingDayTo),
                Views.getText(readingNightFrom), Views.getText(readingNightTo));
    }

    @Override
    public void setLogo(Provider provider) {
        logoView.setImageResource(provider.logoRes);
    }

    @Override
    public void showProviderSettings(Provider provider) {
        startActivity(ProviderSettingsActivity.createIntent(getContext(), provider));
    }

    @Override
    public void setTariffText(String tariff) {
        tariffView.setText(tariff);
    }

    @Override
    public void setReadingUnit(@StringRes int unitResId) {
        unitView.setText(unitResId);
    }

    @Override
    public void setSingleReadingsVisibility(final int visibility) {
        ButterKnife.apply(singleReadingsGroup, (ButterKnife.Action<View>) (view, index) -> view.setVisibility(visibility));
    }

    @Override
    public void setDoubleReadingsVisibility(final int visibility) {
        ButterKnife.apply(doubleReadingsGroups, (ButterKnife.Action<View>) (view, index) -> view.setVisibility(visibility));
    }

    @Override
    public void hideForm() {
        dismiss();
    }

    @Override
    public void showDateFieldError(@StringRes int errorMsg) {
        Animations.shake(dateToView);
        dateToView.setError(errorMsg);
    }

    @Override
    public void showReadingFieldError(Field field, int errorMsgRes) {
        TextInputLayout view = getViewFor(field);
        Animations.shake(view);
        view.setError(getString(errorMsgRes));
        view.requestFocus();
        showKeyboard(view.getEditText());
    }

    @Override
    public void cleanErrorsOnFields() {
        readingFrom.setError(null);
        readingTo.setError(null);
        readingDayFrom.setError(null);
        readingDayTo.setError(null);
        readingNightFrom.setError(null);
        readingNightTo.setError(null);
        dateToView.setError(null);
    }

    @Override
    public void startStoringServiceForSingleReadings(Provider provider) {
        final Intent intent = BillStoringServiceIntentFactory
                .of(getContext(), provider)
                .from(readingFrom.getEditText(), readingTo.getEditText(), dateFromView, dateToView);
        getActivity().startService(intent);
    }

    @Override
    public void startBillActivityForSingleReadings(Provider provider) {
        final Intent intent = BillActivityIntentFactory
                .of(getActivity(), provider)
                .from(readingFrom.getEditText(), readingTo.getEditText(), dateFromView, dateToView);
        getActivity().startActivity(intent);
    }

    @Override
    public void startStoringServiceForDoubleReadings(Provider provider) {
        final Intent intent = BillStoringServiceIntentFactory
                .of(getContext(), provider)
                .from(readingDayFrom.getEditText(), readingDayTo.getEditText(),
                        readingNightFrom.getEditText(), readingNightTo.getEditText(),
                        dateFromView, dateToView);
        getActivity().startService(intent);
    }

    @Override
    public void startBillActivityForDoubleReadings(Provider provider) {
        final Intent intent = BillActivityIntentFactory
                .of(getActivity(), provider)
                .from(readingDayFrom.getEditText(), readingDayTo.getEditText(),
                        readingNightFrom.getEditText(), readingNightTo.getEditText(),
                        dateFromView, dateToView);
        getActivity().startActivity(intent);
    }

    private void setDates(LocalDate fromDate, LocalDate toDate) {
        dateFromView.setText(Dates.format(fromDate, DATE_PATTERN));
        dateToView.setText(Dates.format(toDate, DATE_PATTERN));
    }

    private void setReadingsForAutocomplete(InstantAutoCompleteTextInputEditText autoCompleteEditText, int[] readings) {
        autoCompleteEditText.setAdapter(new PreviousReadingsAdapter(getContext(), readings));
    }

    private TextInputLayout getViewFor(Field field) {
        switch (field) {
            case READING_FROM: return readingFrom;
            case READING_TO: return readingTo;
            case READING_DAY_FROM: return readingDayFrom;
            case READING_DAY_TO: return readingDayTo;
            case READING_NIGHT_FROM: return readingNightFrom;
            case READING_NIGHT_TO: return readingNightTo;
        }
        throw new EnumVariantNotHandledException(field);
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }

}
