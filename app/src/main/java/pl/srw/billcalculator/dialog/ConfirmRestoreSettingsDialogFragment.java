package pl.srw.billcalculator.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.activity.ProviderSettingsFragmentOwner;

public class ConfirmRestoreSettingsDialogFragment extends DialogFragment {

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

    private DialogInterface.OnClickListener onPositiveButton() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProviderSettingsFragmentOwner activity = (ProviderSettingsFragmentOwner) getActivity();
                activity.getProviderSettingsFragment().restoreDefault();
            }
        };
    }

}
