package pl.srw.billcalculator.tester;

import android.support.annotation.StringRes;
import android.support.test.espresso.ViewInteraction;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.tester.interactor.RecyclerViewInteraction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
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

    public SettingsDetailsListItem getPreferenceAtLine(int index) {
        RecyclerViewInteraction listInteraction = onRecyclerViewItem(withId(R.id.settings_details_list), index);
        return new SettingsDetailsListItem(listInteraction, index);
    }

    public SettingsDetailsItem getPreferenceWithTitle(@StringRes int titleId) {
        onView(withId(R.id.settings_details_list)).perform(scrollTo(withChild(withText(titleId))));
        ViewInteraction itemInteraction = onView(withText(titleId));
        return new SettingsDetailsItem(itemInteraction);
    }

    public class SettingsDetailsItem {

        private final ViewInteraction itemInteraction;

        private SettingsDetailsItem(ViewInteraction itemInteraction) {
            this.itemInteraction = itemInteraction;
        }

        public ProviderSettingsTester<T> changeValueTo(String value) {
            itemInteraction.perform(click());
            onView(withId(R.id.settingsDialogInput))
                    .perform(replaceText(value), closeSoftKeyboard());
            clickText(R.string.settings_input_accept);
            return ProviderSettingsTester.this;
        }
    }

    public class SettingsDetailsListItem {

        private final RecyclerViewInteraction viewInteraction;
        private final int position;

        private SettingsDetailsListItem(RecyclerViewInteraction interaction, int position) {
            this.viewInteraction = interaction;
            this.position = position;
        }

        public ProviderSettingsTester<T> hasTitle(String title) {
            viewInteraction.checkView(R.id.title, matches(withText(title)));
            return ProviderSettingsTester.this;
        }

        public void hasSummary(String summary) {
            viewInteraction.checkView(R.id.summary, matches(withText(startsWith(summary))));
        }

        public ProviderSettingsTester<T> changeValueTo(String value) {
            open();
            onView(withId(R.id.settingsDialogInput)).perform(replaceText(value), closeSoftKeyboard());
            clickText(R.string.settings_input_accept);
            return ProviderSettingsTester.this;
        }

        public ProviderSettingsTester<T> pickOption(String option) {
            open();
            onView(allOf(withId(R.id.settingsPickingDialogOption), withText(option))).perform(click());
            return ProviderSettingsTester.this;
        }

        private void open() {
            onView(withId(R.id.settings_details_list)).perform(actionOnItemAtPosition(position, click()));
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
