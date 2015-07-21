package pl.srw.billcalculator.test.form.view;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.MainActivity;
import pl.srw.billcalculator.testutils.HistoryGenerator;
import pl.srw.billcalculator.testutils.PreferenceUtil;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by kseweryn on 21.07.15.
 */
public class InstantAutoCompleteTextViewUITest {

    @Rule
    public final ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        HistoryGenerator.clear();
        PreferenceUtil.changeToG11Tariff(Provider.PGE);
    }

    @Test
    public void shouldShowAutoComplete() {
        // given: entry in history
        HistoryGenerator.generatePgeG11Bill(11);
        // when: activity start
        final MainActivity activity = activityRule.launchActivity(null);
        // then: auto complete frame show up
        onView(isAssignableFrom(TextView.class)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(withText("11")));
    }

    @Test
    public void shouldShowOneCompletionOnDuplicatedEntries() {
        // given: entry in history
        HistoryGenerator.generatePgeG11Bill(25);
        HistoryGenerator.generatePgeG11Bill(25);
        // when: activity start
        final MainActivity activity = activityRule.launchActivity(null);
        // then: auto complete frame show up
        onView(isAssignableFrom(TextView.class)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(withText("25")));
    }

    @Test
    public void shouldShowOnlyHighestValue() {
        // given: many entries in history
        HistoryGenerator.generatePgeG11Bills(20);
        // when: activity start
        final MainActivity activity = activityRule.launchActivity(null);
        // then: auto complete show highest value
        onView(isAssignableFrom(TextView.class)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(withText("30")));
    }

    @Test
    public void shouldShowAllMatchingEntriesWhenTextPartiallyFilled() {
        // given: entries in history
        HistoryGenerator.generatePgeG11Bill(19);
        HistoryGenerator.generatePgeG11Bill(49);
        HistoryGenerator.generatePgeG11Bill(115);
        HistoryGenerator.generatePgeG11Bill(199);
        HistoryGenerator.generatePgeG11Bill(230);
        // when: user enters text in prev reading
        final MainActivity activity = activityRule.launchActivity(null);
        onView(allOf(withId(R.id.et_reading_from), isDisplayed())).perform(typeText("1"));
        // then: auto complete show highest value
        onData(anything()).atPosition(0).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(withText("199")));
        onData(anything()).atPosition(1).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(withText("115")));
        onData(anything()).atPosition(2).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(withText("19")));
    }

}