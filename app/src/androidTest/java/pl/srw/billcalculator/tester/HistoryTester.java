package pl.srw.billcalculator.tester;

import android.support.test.espresso.NoMatchingViewException;

import pl.srw.billcalculator.R;

public class HistoryTester extends Tester {

    public HistoryTester skipCheckPricesDialogIfVisible() {
        try {
            clickText(R.string.check_prices_info_cancel);
        } catch (NoMatchingViewException ex) {
            // ignore
        }
        return this;
    }
}
