package pl.srw.billcalculator.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn on 05.02.2016.
 */
public class ExplainPermissionRequestDialogFragment extends DialogFragment {

    private static final String ARGS_PERMISSIONS = "ARGS_PERMISSIONS";
    private static final String ARGS_REQUEST_CODE = "ARGS_REQUEST_CODE";

    public static ExplainPermissionRequestDialogFragment newInstance(String[] permissions, int requestCode) {
        Bundle args = new Bundle();
        args.putStringArray(ARGS_PERMISSIONS, permissions);
        args.putInt(ARGS_REQUEST_CODE, requestCode);
        ExplainPermissionRequestDialogFragment fragment = new ExplainPermissionRequestDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ExplainPermissionRequestDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.rationale_dialog_title)
                .setMessage(R.string.rationale_dialog_msg)
                .setPositiveButton(R.string.rationale_dialog_ok, onAccepted())
                .setNegativeButton(R.string.rationale_dialog_cancel, null)
                .create();
    }

    private DialogInterface.OnClickListener onAccepted() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int requestCode = getArguments().getInt(ARGS_REQUEST_CODE);
                final String[] permissions = getArguments().getStringArray(ARGS_PERMISSIONS);
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
                    // system dialog for request permission might still occur
                    ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
                } else {
                    goToSettings(requestCode);
                }

            }
        };
    }

    private void goToSettings(int requestCode) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getActivity().getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, requestCode);
    }
}
