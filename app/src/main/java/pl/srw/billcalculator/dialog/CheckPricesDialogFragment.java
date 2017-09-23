package pl.srw.billcalculator.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.settings.activity.SettingsActivity;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.mfvp.MvpFragment;
import pl.srw.mfvp.di.MvpActivityScopedFragment;

public class CheckPricesDialogFragment extends MvpFragment
        implements MvpActivityScopedFragment<HistoryComponent> {

    @Inject SettingsRepo settings;

    @Override
    public void injectDependencies(HistoryComponent historyComponent) {
        historyComponent.inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_check_circle_black_24dp)
                .setTitle(R.string.check_prices_info_title)
                .setMessage(R.string.check_price_info_message)
                .setPositiveButton(R.string.check_prices_info_ok, positiveClickListener())
                .setNegativeButton(R.string.check_prices_info_cancel, negativeClickListener())
                .setOnKeyListener(backButtonListener());
        return builder.create();
    }

    private DialogInterface.OnClickListener positiveClickListener() {
        return (dialog, which) -> {
            Analytics.logContent(ContentType.CHECK_PRICES, "proceed", true);
            getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
            markDialogProcessed();
        };
    }

    private DialogInterface.OnClickListener negativeClickListener() {
        return (dialog, which) -> {
            Analytics.logContent(ContentType.CHECK_PRICES, "proceed", false);
            markDialogProcessed();
        };
    }

    private DialogInterface.OnKeyListener backButtonListener() {
        return (dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                Analytics.logContent(ContentType.CHECK_PRICES, "proceed", false);
                markDialogProcessed();
            }
            return false;
        };
    }

    private void markDialogProcessed() {
        settings.markFirstLaunch();
    }
}
