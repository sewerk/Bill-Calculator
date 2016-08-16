package pl.srw.billcalculator.tester;

import android.support.test.espresso.ViewInteraction;
import android.view.View;

import org.hamcrest.Matcher;

import pl.srw.billcalculator.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class SettingsTester extends Tester {

    public static final int PGE = 0;
    public static final int PGNIG = PGE + 1;
    public static final int TAURON = PGNIG + 1;


    public SettingsTester openSettings() {
        openDrawer();
        clickDrawerMenu("Settings");
        return this;
    }

    public SettingsTester pickProvider(int provider) {
        ViewInteraction settingsItem = onView(
                allOf(withId(R.id.settings_list_item),
                        childAtPosition(withId(R.id.list), provider)));
        settingsItem.perform(click());
        return this;
    }

    public Preference getPreferenceLineAt(int index) {
        return new Preference(childAtPosition(
                        childAtPosition(withId(android.R.id.list), index),
                        1));
    }

    public SettingsTester restoreDefault() {
        clickIcon("Restore default");
        clickText("Yes");
        return this;
    }

    public class Preference {

        private Matcher<View> preferenceView;

        public Preference(Matcher<View> preferenceView) {
            this.preferenceView = preferenceView;
        }

        public void hasTitle(String title) {
            onView(allOf(withId(android.R.id.title), withParent(preferenceView)))
                    .check(matches(withText(title)));
        }

        public void hasSummary(String summary) {
            onView(allOf(withId(android.R.id.summary), withParent(preferenceView)))
                    .check(matches(withText(startsWith(summary))));
        }

        public void changeValueTo(String value) {
            onView(preferenceView).perform(click());
            onView(allOf(withId(android.R.id.edit), withParent(withClassName(is("android.widget.LinearLayout")))))
                    .perform(scrollTo(), replaceText(value), closeSoftKeyboard());
            clickText("OK");
        }

        public void pickOption(String option) {
            onView(preferenceView).perform(click());
            onView(allOf(withId(android.R.id.text1), withText(option))).perform(click());
        }
    }
}
