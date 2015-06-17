package pl.srw.billcalculator.test;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.HistoryActivity;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.testutils.HistoryGenerator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;


/**
 * Created by kseweryn on 08.04.15.
 */
@MediumTest
public class HistoryUITest {

    @Rule public ActivityTestRule<HistoryActivity> activity = new ActivityTestRule<>(HistoryActivity.class);

    @Before
    public void setUp() throws Exception {
        GeneralPreferences.markFirstLaunch();
        HistoryGenerator.clear();
    }

    @Test
    public void shouldRemoveBillWithPricesFromDb() {
        // given: one bill in history
        HistoryGenerator.generatePgeG11Bills(1);
        activity.getActivity();

        // when: deleting one bill
        onView(withText("1 - 11")).perform(longClick());
        onView(withId(R.id.action_delete)).perform(click());

        // then: no bill is history is available
        onView(withText(R.string.empty_history)).check(matches(isDisplayed()));
        // and no bill and prices in database is available
        assertTrue(Database.getSession().getPgeG11BillDao().loadAll().isEmpty());
        assertTrue(Database.getSession().getPgePricesDao().loadAll().isEmpty());
    }

    @Test
    public void shouldUnselectAfterDeletion() {
        // given: list contain 5 entries
        HistoryGenerator.generatePgeG11Bills(5);
        activity.getActivity();

        // when: select second entry and delete
        onView(withText("4 - 14")).perform(longClick());
        onImageInRow(withText("4 - 14")).check(matches(withTagValue(is((Object) R.drawable.selected))));
        onView(withId(R.id.action_delete)).perform(click());
        // and select second and third entry and delete
        onView(withText("3 - 13")).perform(longClick());
        onView(withText("2 - 12")).perform(click());
        onView(withId(R.id.action_delete)).perform(click());

        // then: none item is selected
        onImageInRow(withText("1 - 11")).check(matches(withTagValue(is((Object) R.drawable.pge))));
        onImageInRow(withText("5 - 15")).check(matches(withTagValue(is((Object) R.drawable.pge))));
    }

    @Test
    public void shouldUselectAfterDone() throws InterruptedException {
        // given: list contain 3 entries
        HistoryGenerator.generatePgeG11Bills(3);
        activity.getActivity();

        // when: selecting 1st and 3rd entry
        onView(withText("1 - 11")).perform(longClick());
        onView(withText("3 - 13")).perform(click());
        onImageInRow(withText("1 - 11")).check(matches(withTagValue(is((Object) R.drawable.selected))));
        onImageInRow(withText("3 - 13")).check(matches(withTagValue(is((Object) R.drawable.selected))));

        // and clicking done
        int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");
        onView(withId(doneButtonId)).perform(click());

        // then: none entry is selected
        onImageInRow(withText("1 - 11")).check(matches(withTagValue(is((Object) R.drawable.pge))));
        onImageInRow(withText("2 - 12")).check(matches(withTagValue(is((Object) R.drawable.pge))));
        onImageInRow(withText("3 - 13")).check(matches(withTagValue(is((Object) R.drawable.pge))));
    }

    @Test
    public void shouldRestoreSelectionOnScreenRotation() throws InterruptedException {
        // given: one item is selected
        HistoryGenerator.generatePgeG11Bills(3);
        HistoryActivity activity = this.activity.getActivity();
        onView(withText("2 - 12")).perform(longClick());
        onImageInRow(withText("2 - 12")).check(matches(withTagValue(is((Object) R.drawable.selected))));

        // when: screen orientation change
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // then: select mode is active and item is selected
        onView(withId(R.id.action_delete)).check(matches(isDisplayed()));
        onImageInRow(withText("2 - 12")).check(matches(withTagValue(is((Object) R.drawable.selected))));
    }

    private ViewInteraction onImageInRow(Matcher<View> siblingMatcher) {
        return onView(allOf(withId(R.id.iv_bill_type), hasSibling(withChild(siblingMatcher))));
    }
}
