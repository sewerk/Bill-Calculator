package pl.srw.billcalculator.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.billcalculator.type.ActionType;

/**
 * Created by kseweryn on 18.06.15.
 */
public class ConfirmRestoreSettingsDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_warning)
            .setTitle(getString(R.string.action_restore))
            .setMessage(getString(R.string.confirm_restore_prices_message))
            .setPositiveButton(R.string.restore_prices_confirm, onPositiveButton())
            .setNegativeButton(R.string.restore_prices_cancel, null);
        return builder.create();
    }

    private DialogInterface.OnClickListener onPositiveButton() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProviderSettingsActivity activity = (ProviderSettingsActivity) getActivity();
                activity.getProviderSettingsFragment().restoreDefault();
                AnalyticsWrapper.logAction(ActionType.RESTORE_PRICES, "Default prices restored", activity.getProviderName());
            }
        };
    }

}
