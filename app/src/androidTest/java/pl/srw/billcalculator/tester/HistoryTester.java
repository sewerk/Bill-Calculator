package pl.srw.billcalculator.tester;

import pl.srw.billcalculator.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class HistoryTester extends Tester {

    private AppTester parent;
    private BillTester billTester = new BillTester(parent);

    HistoryTester(AppTester parent) {
        this.parent = parent;
    }

    public BillTester openBillWithReadings(String from, String to) {
        clickText(from + " - " + to);
        return billTester;
    }

    public HistoryTester changeItemSelectionAtPosition(int position) {
        onView(withId(R.id.bill_list)).perform(scrollToPosition(position + 1));
        onView(withId(R.id.bill_list)).perform(actionOnItemAtPosition(position, longClick()));
        return this;
    }

    public HistoryTester changeItemSelectionWithReadings(String from, String to) {
        onView(allOf(withId(R.id.my_bills_item_logo), hasSibling(withText(from + " - " + to))))
                .perform(click());
        return this;
    }

    public HistoryTester deleteSelected() {
        clickView(R.id.action_delete);
        return this;
    }

    public HistoryTester checkEmptyHistoryIsShown() {
        onView(withText(R.string.empty_history)).check(matches(isDisplayed()));
        return this;
    }

    public HistoryTester checkItemSelected(int position) {
        onView(withId(R.id.bill_list)).perform(scrollToPosition(position + 1));
        onView(allOf(withId(R.id.my_bills_item_logo),
                withParent(withParent(childAtPosition(withId(R.id.bill_list), position)))))
                .check(matches(withImageDrawable(R.drawable.history_item_selected)));
        return this;
    }

    public HistoryTester checkNoSelection() {
        onView(withImageDrawable(R.drawable.history_item_selected)).check(doesNotExist());
        return this;
    }

    public HistoryTester checkDeleteButtonShown() {
        onView(withId(R.id.action_delete)).check(matches(isDisplayed()));
        return this;
    }

    public HistoryTester checkDeleteButtonHidden() {
        onView(withId(R.id.action_delete)).check(doesNotExist());
        return this;
    }
}
