package pl.srw.billcalculator.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import pl.srw.billcalculator.R;

public class BillCalculatedBeforeOZEChangeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_bill_without_oze_title))
                .setMessage(getString(R.string.dialog_bill_without_oze_message))
                .setPositiveButton(R.string.dialog_bill_without_oze_positive, null);
        return builder.create();
    }
}