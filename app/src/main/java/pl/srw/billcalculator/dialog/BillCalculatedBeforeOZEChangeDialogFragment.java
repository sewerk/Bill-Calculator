package pl.srw.billcalculator.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.HistoryActivity;


public class BillCalculatedBeforeOZEChangeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_bill_without_oze_title))
                .setMessage(getString(R.string.dialog_bill_without_oze_message))
                .setPositiveButton(R.string.dialog_bill_without_oze_positive, onPositiveButton())
                .setNegativeButton(R.string.dialog_bill_without_oze_cancel, null);
        return builder.create();
    }

    private DialogInterface.OnClickListener onPositiveButton() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                getActivity().setResult(HistoryActivity.REQUEST_RESULT_RECALCULATE_BILL);
//                getActivity().finish();
            }
        };
    }
}
