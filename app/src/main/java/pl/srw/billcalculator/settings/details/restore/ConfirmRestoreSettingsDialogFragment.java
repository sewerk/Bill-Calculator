package pl.srw.billcalculator.settings.details.restore;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.di.Dependencies;
import pl.srw.billcalculator.type.Provider;

public class ConfirmRestoreSettingsDialogFragment extends DialogFragment {

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
        Provider provider = Provider.values()[providerIdx];
        Dependencies.INSTANCE.inject(this);
        presenter.setup(provider);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_warning_black_24dp)
            .setTitle(R.string.action_restore)
            .setMessage(R.string.confirm_restore_prices_message)
            .setPositiveButton(R.string.restore_prices_confirm, (d, w) -> presenter.onConfirmClicked())
            .setNegativeButton(R.string.restore_prices_cancel, (d, w) -> presenter.onCancelClicked());
        return builder.create();
    }
}
