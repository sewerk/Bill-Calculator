package pl.srw.billcalculator.form.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.di.FormComponent;
import pl.srw.billcalculator.form.view.RoundedLogoView;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.billcalculator.type.Provider;
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
    @BindView(R.id.form_entry_dates_from) TextView dateFromView;
    @BindView(R.id.form_entry_dates_to) TextView dateToView;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(), presenter.getFormLayout(), null);
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
        dismiss(); // TODO: use presenter
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
}
