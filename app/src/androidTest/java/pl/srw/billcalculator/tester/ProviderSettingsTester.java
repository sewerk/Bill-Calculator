package pl.srw.billcalculator.tester;

import android.support.annotation.StringRes;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
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

public class ProviderSettingsTester<T extends Tester> extends Tester {

    private final T parent;

    public ProviderSettingsTester(T parent) {
        this.parent = parent;
    }

    public ProviderSettingsTester<T> restoreDefault() {
        clickIcon("Restore default");
        clickText("Yes");
        return this;
    }

    public T close() {
        pressBack();
        return parent;
    }

    public Preference getPreferenceAtLine(int index) {
        return new Preference(childAtPosition(
                childAtPosition(withId(android.R.id.list), index),
                1));
    }

    public Preference getPreference(@StringRes int id) {
        return new Preference(withText(id));
    }

    public class Preference {

        private Matcher<View> preferenceView;

        public Preference(Matcher<View> preferenceView) {
            this.preferenceView = preferenceView;
        }

        public ProviderSettingsTester<T> hasTitle(String title) {
            onView(allOf(withId(android.R.id.title), withParent(preferenceView)))
                    .check(matches(withText(title)));
            return ProviderSettingsTester.this;
        }

        public void hasSummary(String summary) {
            onView(allOf(withId(android.R.id.summary), withParent(preferenceView)))
                    .check(matches(withText(startsWith(summary))));
        }

        public ProviderSettingsTester<T> changeValueTo(String value) {
            onView(preferenceView).perform(click());
            onView(allOf(withId(android.R.id.edit), withParent(withClassName(is("android.widget.LinearLayout")))))
                    .perform(scrollTo(), replaceText(value), closeSoftKeyboard());
            clickText("OK");
            return ProviderSettingsTester.this;
        }

        public ProviderSettingsTester<T> pickOption(String option) {
            onView(preferenceView).perform(click());
            onView(allOf(withId(android.R.id.text1), withText(option))).perform(click());
            return ProviderSettingsTester.this;
        }
    }
}
