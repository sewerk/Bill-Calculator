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
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.Arrays;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.save.BillSaver;
import pl.srw.billcalculator.databinding.FormBinding;
import pl.srw.billcalculator.di.Dependencies;
import pl.srw.billcalculator.form.FormPreviousReadingsVM;
import pl.srw.billcalculator.form.FormVM;
import pl.srw.billcalculator.form.FormVMFactory;
import pl.srw.billcalculator.form.autocomplete.PreviousReadingsAdapter;
import pl.srw.billcalculator.form.view.DatePickingView;
import pl.srw.billcalculator.form.view.InstantAutoCompleteTextInputEditText;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.settings.SettingsActivity;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Animations;
import pl.srw.billcalculator.util.analytics.Analytics;
import pl.srw.billcalculator.util.analytics.ContentType;
import timber.log.Timber;

public class FormFragment extends DialogFragment implements FormPresenter.FormView {

    public static final String DATE_PATTERN = "dd.MM.yy";
    private static final String EXTRA_PROVIDER = "PROVIDER";

    @Inject FormVMFactory formVMFactory;
    @Inject FormPresenter.HistoryChangeListener historyUpdater;
    @Inject BillSaver billSaver;

    private DatePickingView dateToView;
    private TextInputLayout readingFrom;
    private InstantAutoCompleteTextInputEditText readingFromInput;
    private TextInputLayout readingTo;
    private TextInputLayout readingDayFrom;
    private InstantAutoCompleteTextInputEditText readingDayFromInput;
    private TextInputLayout readingDayTo;
    private TextInputLayout readingNightFrom;
    private InstantAutoCompleteTextInputEditText readingNightFromInput;
    private TextInputLayout readingNightTo;

    private FormVM formVM;
    private FormPreviousReadingsVM formPrevReadingsVM;
    private FormPresenter presenter;

    private final DialogInterface.OnKeyListener onBackListener = (dialog, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            Timber.i("Form: back pressed");
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
        Dependencies.INSTANCE.inject(this);

        final int providerIdx = getArguments().getInt(EXTRA_PROVIDER);
        final Provider provider = Provider.values()[providerIdx];
        Analytics.contentView(ContentType.FORM, "New bill form", provider);
        presenter = new FormPresenter(this, provider, billSaver, historyUpdater);

        formVMFactory.setProvider(provider);
        formVMFactory.setBundle(savedInstanceState);
        formVM = ViewModelProviders.of(this, formVMFactory).get(FormVM.class);
        formPrevReadingsVM = ViewModelProviders.of(this, formVMFactory).get(FormPreviousReadingsVM.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.form, null);
        FormBinding binding = FormBinding.bind(view);
        binding.setVm(formVM);
        binding.setPresenter(presenter);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(onBackListener);

        bindViews(binding);
        setupSettingsLink(binding.header.formSettingsLink);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        formVM.getOpenSettingsCommand().observe(this, this::showProviderSettings);
        formPrevReadingsVM.getSinglePrevReadings().observe(this, readings -> setReadingsForAutocomplete(readingFromInput, readings));
        formPrevReadingsVM.getDayPrevReadings().observe(this, readings -> setReadingsForAutocomplete(readingDayFromInput, readings));
        formPrevReadingsVM.getNightPrevReadings().observe(this, readings -> setReadingsForAutocomplete(readingNightFromInput, readings));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        formVM.writeTo(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbindViews();
    }

    @Override
    public void showProviderSettings(@NonNull Provider provider) {
        Analytics.contentView(ContentType.SETTINGS, "settings from", "Form", "settings for", provider);
        SettingsActivity.start(getContext(), provider);
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
    public void showReadingFieldError(@NonNull Field field, int errorMsgRes) {
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
    public void startBillActivity(@NonNull Provider provider) {
        final Intent intent = BillActivityIntentFactory
                .of(getActivity(), provider)
                .from(formVM);
        getActivity().startActivity(intent);
    }

    private void setReadingsForAutocomplete(InstantAutoCompleteTextInputEditText autoCompleteEditText, int[] readings) {
        Timber.d("Previous readings for autocomplete: %s", Arrays.toString(readings));
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

    private void setupSettingsLink(TextView settingsLink) {
        final String settingsLabel = settingsLink.getText().toString();
        final SpannableString span = new SpannableString(settingsLabel);
        span.setSpan(new UnderlineSpan(), 0, settingsLabel.length(), 0);
        settingsLink.setText(span);
        settingsLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }

    private void bindViews(FormBinding binding) {
        dateToView = binding.dates.formEntryDatesTo;
        readingFrom = binding.singleReadings.formEntryReadingFrom;
        readingFromInput = binding.singleReadings.formEntryReadingFromInput;
        readingTo = binding.singleReadings.formEntryReadingTo;
        readingDayFrom = binding.doubleReadings.formEntryReadingDayFrom;
        readingDayFromInput = binding.doubleReadings.formEntryReadingDayFromInput;
        readingDayTo = binding.doubleReadings.formEntryReadingDayTo;
        readingNightFrom = binding.doubleReadings.formEntryReadingNightFrom;
        readingNightFromInput = binding.doubleReadings.formEntryReadingNightFromInput;
        readingNightTo = binding.doubleReadings.formEntryReadingNightTo;
    }

    private void unbindViews() {
        dateToView = null;
        readingFrom = null;
        readingFromInput = null;
        readingTo = null;
        readingDayFrom = null;
        readingDayFromInput = null;
        readingDayTo = null;
        readingNightFrom = null;
        readingNightFromInput = null;
        readingNightTo = null;
    }
}
