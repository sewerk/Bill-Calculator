package pl.srw.billcalculator.component;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import java.util.Date;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn
 */
public class CheckPricesDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_menu_edit)
                .setTitle(getString(R.string.check_prices_info_title))
                .setMessage(getString(R.string.check_price_info_message))
                .setPositiveButton(R.string.check_prices_info_ok, positiveClickListener())
                .setNegativeButton(getString(R.string.check_prices_info_cancel), negativeClickListener())
                .setOnKeyListener(backButtonListener());
        return builder.create();
    }

    private DialogInterface.OnClickListener positiveClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity)getActivity()).startSettings();
                markDialogProcessed();
            }
        };
    }

    private DialogInterface.OnClickListener negativeClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                markDialogProcessed();
            }
        };
    }

    private DialogInterface.OnKeyListener backButtonListener() {
        return new DialogInterface.OnKeyListener() {
            @Override
            @DebugLog
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    markDialogProcessed();
                }
                return false;
            }
        };
    }

    @DebugLog
    private void markDialogProcessed() {
        getActivity().getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit().putString(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH, new Date().toString()).commit();
    }
}
