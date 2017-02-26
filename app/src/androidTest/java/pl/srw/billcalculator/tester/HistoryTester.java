package pl.srw.billcalculator.tester;

import android.support.test.espresso.action.ViewActions;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class HistoryTester extends Tester {

    private AppTester parent;
    private BillTester billTester = new BillTester(parent);

    public HistoryTester(AppTester parent) {
        this.parent = parent;
    }

    public BillTester openBillWithReadings(String from, String to) {
        clickText(from + " - " + to);
        return billTester;
    }

    public BillTester removeBillWithReadings(String from, String to) {
        onView(withText(from + " - " + to)).perform(ViewActions.swipeRight());
        return billTester;
    }
}
