package pl.srw.billcalculator.form.fragment;

import android.app.Dialog;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.di.FormComponent;
import pl.srw.billcalculator.form.view.DatePickingView;
import pl.srw.billcalculator.form.view.RoundedLogoView;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Animations;
import pl.srw.mfvp.MvpFragment;
import pl.srw.mfvp.view.delegate.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.view.delegate.presenter.PresenterOwner;
import pl.srw.mfvp.view.delegate.presenter.SinglePresenterHandlingDelegate;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

public class FormFragment extends MvpFragment
        implements MvpFragmentScopedFragment<FormComponent, HistoryComponent>,
        PresenterOwner, FormPresenter.FormView {

    private static final String EXTRA_PROVIDER = "PROVIDER";

    @Inject
    FormPresenter presenter;

    @BindView(R.id.form_logo) RoundedLogoView logoView;
    @BindView(R.id.form_settings_link) TextView settingsLink;
    @BindView(R.id.form_entry_tariff) TextView tariffView;
    @BindView(R.id.form_entry_reading_unit) TextView unitView;
    @BindView(R.id.form_entry_readings_single) ViewGroup singleReadingsGroup;
    @BindViews({R.id.form_entry_readings_day, R.id.form_entry_readings_night}) ViewGroup[] doubleReadingsGroups;
    @BindView(R.id.form_entry_dates_from) DatePickingView dateFromView;
    @BindView(R.id.form_entry_dates_to) DatePickingView dateToView;

    @BindView(R.id.form_entry_reading_from) TextInputLayout readingFrom;
    @BindView(R.id.form_entry_reading_to) TextInputLayout readingTo;
    @BindView(R.id.form_entry_reading_day_from) TextInputLayout readingDayFrom;
    @BindView(R.id.form_entry_reading_day_to) TextInputLayout readingDayTo;
    @BindView(R.id.form_entry_reading_night_from) TextInputLayout readingNightFrom;
    @BindView(R.id.form_entry_reading_night_to) TextInputLayout readingNightTo;

    private Unbinder unbinder;

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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.form, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        unbinder = ButterKnife.bind(this, view);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public FormComponent getFragmentComponent(HistoryComponent historyComponent) {
        return historyComponent.getFormComponent();
    }

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }

    @Override
    public void setupSettingsLink() {
        final String settingsLabel = settingsLink.getText().toString();
        final SpannableString span = new SpannableString(settingsLabel);
        span.setSpan(new UnderlineSpan(), 0, settingsLabel.length(), 0);
        settingsLink.setText(span);
        settingsLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick(R.id.form_settings_link)
    void onSettingsLinkClicked() {
        presenter.settingsLinkClicked();
    }

    @OnClick(R.id.close_button)
    void closeButtonClicked() {
        presenter.closeButtonClicked();
    }

    @OnClick(R.id.calculate_button)
    void calculateButtonClicked() {
        presenter.calculateButtonClicked(getText(readingFrom), getText(readingTo),
                getText(dateFromView), getText(dateToView),
                getText(readingDayFrom), getText(readingDayTo),
                getText(readingNightFrom), getText(readingNightTo));
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
    public void setDates(String fromDate, String toDate) {
        dateFromView.setText(fromDate);
        dateToView.setText(toDate);
    }

    @Override
    public void setSingleReadingsVisibility(int visibility) {
        singleReadingsGroup.setVisibility(visibility);
    }

    @Override
    public void setDoubleReadingsVisibility(final int visibility) {
        ButterKnife.apply(doubleReadingsGroups, new ButterKnife.Action<ViewGroup>() {
            @Override
            public void apply(@NonNull ViewGroup view, int index) {
                view.setVisibility(visibility);
            }
        });
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

    private static String getText(TextInputLayout view) {
        return view.getEditText().getText().toString();
    }

    private static String getText(TextView view) {
        return view.getText().toString();
    }
}
