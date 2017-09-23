package pl.srw.billcalculator.settings.restore;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.activity.ProviderSettingsFragmentOwner;
import pl.srw.billcalculator.settings.di.ConfirmRestoreSettingsComponentInjectable;
import pl.srw.billcalculator.type.Provider;
import pl.srw.mfvp.MvpFragment;
import pl.srw.mfvp.di.MvpActivityScopedFragment;

public class ConfirmRestoreSettingsDialogFragment extends MvpFragment
        implements MvpActivityScopedFragment<ConfirmRestoreSettingsComponentInjectable>,
        ConfirmRestoreSettingsPresenter.ConfirmRestoreSettingsView {

    private static final String EXTRA_PROVIDER = "EXTRA_PROVIDER";
    @Inject ConfirmRestoreSettingsPresenter presenter;

    public static ConfirmRestoreSettingsDialogFragment newInstance(Provider provider) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_PROVIDER, provider.ordinal());
        ConfirmRestoreSettingsDialogFragment fragment = new ConfirmRestoreSettingsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int providerIdx = getArguments().getInt(EXTRA_PROVIDER);
        final Provider provider = Provider.values()[providerIdx];
        presenter.setup(provider);
        attachPresenter(presenter);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_warning_black_24dp)
            .setTitle(R.string.action_restore)
            .setMessage(R.string.confirm_restore_prices_message)
            .setPositiveButton(R.string.restore_prices_confirm, onPositiveButton())
            .setNegativeButton(R.string.restore_prices_cancel, null);
        return builder.create();
    }

    @Override
    public void injectDependencies(ConfirmRestoreSettingsComponentInjectable settingsComponent) {
        settingsComponent.inject(this);
    }

    @Override
    public void refresh() {
        ProviderSettingsFragmentOwner activity = (ProviderSettingsFragmentOwner) getActivity();
        activity.getProviderSettingsFragment().refreshScreen();
    }

    private DialogInterface.OnClickListener onPositiveButton() {
        return (dialog, which) -> presenter.onConfirmedClicked();
    }

}
