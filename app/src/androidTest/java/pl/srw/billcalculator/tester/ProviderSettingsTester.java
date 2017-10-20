package pl.srw.billcalculator.tester;

import android.preference.EditTextPreference;
import android.support.annotation.StringRes;
import android.support.test.espresso.DataInteraction;

import pl.srw.billcalculator.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class ProviderSettingsTester<T extends Tester> extends Tester {

    private final T parent;
    private final HelpTester helpTester = new HelpTester(this);

    ProviderSettingsTester(T parent) {
        this.parent = parent;
    }

    public ProviderSettingsTester<T> restoreDefault() {
        clickIcon(R.string.action_restore);
        clickText(R.string.restore_prices_confirm);
        return this;
    }

    public HelpTester openHelp() {
        clickIcon(R.string.action_help);
        return helpTester;
    }

    public T close() {
        pressBack();
        return parent;
    }

    public Preference getPreferenceAtLine(int index) {
        DataInteraction dataInteraction = onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .atPosition(index);
        return new Preference(dataInteraction);
    }

    public Preference getPreferenceWithTitle(@StringRes int id) {
        DataInteraction dataInteraction = onData(allOf(instanceOf(EditTextPreference.class), withTitle(id)));
        return new Preference(dataInteraction);
    }

    public class Preference {

        private DataInteraction dataInteraction;

        private Preference(DataInteraction dataInteraction) {
            this.dataInteraction = dataInteraction;
        }

        public ProviderSettingsTester<T> hasTitle(String title) {
            dataInteraction
                    .onChildView(withId(android.R.id.title))
                    .check(matches(withText(title)));
            return ProviderSettingsTester.this;
        }

        public void hasSummary(String summary) {
            dataInteraction
                    .onChildView(withId(android.R.id.summary))
                    .check(matches(withText(startsWith(summary))));
        }

        public ProviderSettingsTester<T> changeValueTo(String value) {
            dataInteraction.perform(click());
            onView(allOf(withId(android.R.id.edit), withParent(withClassName(is("android.widget.LinearLayout")))))
                    .perform(scrollTo(), replaceText(value), closeSoftKeyboard());
            clickText("OK");
            return ProviderSettingsTester.this;
        }

        public ProviderSettingsTester<T> pickOption(String option) {
            dataInteraction.perform(click());
            onView(allOf(withId(android.R.id.text1), withText(option))).perform(click());
            return ProviderSettingsTester.this;
        }
    }

    public class HelpTester {

        private final ProviderSettingsTester<T> parent;

        public HelpTester(ProviderSettingsTester<T> parent) {
            this.parent = parent;
        }

        public ProviderSettingsTester<T> clickOk() {
            clickText("OK");
            return parent;
        }
    }
}
