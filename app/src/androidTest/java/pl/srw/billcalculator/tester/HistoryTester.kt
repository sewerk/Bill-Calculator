package pl.srw.billcalculator.tester

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.hasSibling
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isSelected
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import pl.srw.billcalculator.R

internal class HistoryTester internal constructor(parent: AppTester) : Tester() {

    private val billTester: BillTester = BillTester(parent)
    private var lastPosition: Int = 0

    fun openBillWithReadings(from: String, to: String): BillTester {
        clickText("$from - $to")
        return billTester
    }

    fun openBillAtPosition(position: Int): BillTester {
        onView(withId(R.id.bill_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
        return billTester
    }

    fun changeItemSelectionAtPosition(position: Int): HistoryTester {
        scrollToIndex(position)
        onView(withId(R.id.bill_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, longClick()))
        return this
    }

    fun changeItemSelectionWithReadings(from: String, to: String): HistoryTester {
        onView(allOf(withId(R.id.history_item_logo), hasSibling(withText("$from - $to"))))
            .perform(click())
        return this
    }

    fun deleteBillWithReadings(from: String, to: String): HistoryTester {
        onView(withText("$from - $to"))
            .perform(swipeAwayRight())
        return this
    }

    fun deleteSelected(): HistoryTester {
        clickView(R.id.action_delete)
        return this
    }

    fun undoDelete(): HistoryTester {
        clickText(R.string.action_undo_delete)
        return this
    }

    fun checkEmptyHistoryIsShown(): HistoryTester {
        onView(withText(R.string.empty_history))
            .check(matches(isDisplayed()))
        return this
    }

    fun checkEmptyHistoryIsNotShown(): HistoryTester {
        onView(withText(R.string.empty_history))
            .check(matches(not(isDisplayed())))
        return this
    }

    fun checkUndoMessageIsShown(): HistoryTester {
        onView(withText(R.string.bill_deleted))
            .check(matches(isDisplayed()))
        return this
    }

    fun checkItemSelected(position: Int): HistoryTester {
        scrollToIndex(position)
        onRecyclerViewItem(withId(R.id.bill_list), position)
            .checkView(R.id.history_item_logo, matches(isSelected()))
        return this
    }

    fun checkItemNotSelected(position: Int): HistoryTester {
        scrollToIndex(position)
        onRecyclerViewItem(withId(R.id.bill_list), position)
            .checkView(R.id.history_item_logo, matches(not(isSelected())))
        return this
    }

    fun checkNoSelection(): HistoryTester {
        onView(isSelected())
            .check(doesNotExist())
        return this
    }

    fun checkDeleteButtonShown(): HistoryTester {
        onView(withId(R.id.action_delete))
            .check(matches(isDisplayed()))
        return this
    }

    fun checkDeleteButtonHidden(): HistoryTester {
        onView(withId(R.id.action_delete))
            .check(doesNotExist())
        return this
    }

    fun checkItemReadings(position: Int, firstLine: String, secondLine: String) {
        scrollToIndex(position)
        onRecyclerViewItem(withId(R.id.bill_list), position)
            .checkView(R.id.history_item_day_readings, matches(withText(firstLine)))
            .checkView(R.id.history_item_night_readings, matches(withText(secondLine)))
    }

    private fun scrollToIndex(position: Int) {
        var index = if (position > lastPosition) position + 1
        else position - 1

        if (index < 0) index = 0

        onView(withId(R.id.bill_list))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(index))
        lastPosition = position
    }
}
