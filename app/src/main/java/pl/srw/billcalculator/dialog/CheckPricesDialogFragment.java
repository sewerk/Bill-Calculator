package pl.srw.billcalculator.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.data.ApplicationRepo;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.settings.SettingsActivity;
import pl.srw.billcalculator.util.analytics.Analytics;
import pl.srw.billcalculator.util.analytics.EventType;
import pl.srw.mfvp.MvpFragment;
import pl.srw.mfvp.di.MvpActivityScopedFragment;

public class CheckPricesDialogFragment extends MvpFragment
        implements MvpActivityScopedFragment<HistoryComponent> {

    @Inject ApplicationRepo dataRepo;

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
            Analytics.event(EventType.CHECK_PRICES, "proceed", true);
            SettingsActivity.start(getContext());
            markDialogProcessed();
        };
    }

    private DialogInterface.OnClickListener negativeClickListener() {
        return (dialog, which) -> {
            Analytics.event(EventType.CHECK_PRICES, "proceed", false);
            markDialogProcessed();
        };
    }

    private DialogInterface.OnKeyListener backButtonListener() {
        return (dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                Analytics.event(EventType.CHECK_PRICES, "proceed", false);
                markDialogProcessed();
            }
            return false;
        };
    }

    private void markDialogProcessed() {
        dataRepo.markFirstLaunch();
    }
}
