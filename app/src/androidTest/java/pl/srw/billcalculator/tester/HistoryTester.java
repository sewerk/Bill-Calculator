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
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class HistoryTester extends Tester {

    private final AppTester parent;
    private final BillTester billTester;

    HistoryTester(AppTester parent) {
        this.parent = parent;
        billTester = new BillTester(parent);
    }

    public BillTester openBillWithReadings(String from, String to) {
        clickText(from + " - " + to);
        return billTester;
    }

    public BillTester openBillAtPosition(int position) {
        onView(withId(R.id.bill_list)).perform(actionOnItemAtPosition(position, click()));
        return billTester;
    }

    public HistoryTester changeItemSelectionAtPosition(int position) {
        onView(withId(R.id.bill_list)).perform(scrollToPosition(position + 1));
        onView(withId(R.id.bill_list)).perform(actionOnItemAtPosition(position, longClick()));
        return this;
    }

    public HistoryTester changeItemSelectionWithReadings(String from, String to) {
        onView(allOf(withId(R.id.history_item_logo), hasSibling(withText(from + " - " + to))))
                .perform(click());
        return this;
    }

    public HistoryTester deleteBillWithReadings(String from, String to) {
        onView(withText(from + " - " + to)).perform(swipeAwayRight());
        return this;
    }

    public HistoryTester deleteSelected() {
        clickView(R.id.action_delete);
        return this;
    }

    public HistoryTester undoDelete() {
        clickText(R.string.action_undo_delete);
        return this;
    }

    public HistoryTester checkEmptyHistoryIsShown() {
        onView(withText(R.string.empty_history)).check(matches(isDisplayed()));
        return this;
    }

    public HistoryTester checkEmptyHistoryIsNotShown() {
        onView(withText(R.string.empty_history)).check(matches(not(isDisplayed())));
        return this;
    }

    public HistoryTester checkUndoMessageIsShown() {
        onView(withText(R.string.bill_deleted)).check(matches(isDisplayed()));
        return this;
    }

    public HistoryTester checkItemSelected(int position) {
        onView(withId(R.id.bill_list)).perform(scrollToPosition(position + 1));
        onRecyclerViewItem(withId(R.id.bill_list), position)
                .checkView(R.id.history_item_logo, matches(isSelected()));
        return this;
    }

    public HistoryTester checkItemNotSelected(int position) {
        onView(withId(R.id.bill_list)).perform(scrollToPosition(position + 1));
        onRecyclerViewItem(withId(R.id.bill_list), position)
                .checkView(R.id.history_item_logo, matches(not(isSelected())));
        return this;
    }

    public HistoryTester checkNoSelection() {
        onView(isSelected()).check(doesNotExist());
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

    public void checkItemReadings(final int position, final String firstLine, final String secondLine) {
        onView(withId(R.id.bill_list)).perform(scrollToPosition(position + 1));
        onRecyclerViewItem(withId(R.id.bill_list), position)
                .checkView(R.id.history_item_day_readings, matches(withText(firstLine)))
                .checkView(R.id.history_item_night_readings, matches(withText(secondLine)));
    }
}
