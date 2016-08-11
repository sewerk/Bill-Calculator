package pl.srw.billcalculator;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProviderSettingsAndroidTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(DrawerActivity.class);

    @Test
    public void providerSettingsTariffChangeCauseScreenChangeDisplayedValues() {
        openDrawer();
        clickDrawerMenu("Settings");
        clickSettingsListItemAt(0);
        clickPreferenceListAt(0);
        pickPreferenceOption("Taryfa dwustrefowa (G12)");

        checkTitleAtPreference("za energię czynną (strefa dzienna)", 1);

        clickPreferenceListAt(0);
        pickPreferenceOption("Taryfa całodobowa (G11)");

        checkTitleAtPreference("za energię czynną", 1);
    }

    @Test
    public void settingPreferenceValueUpdatedSummary() {
        openDrawer();
        clickDrawerMenu("Settings");
        clickSettingsListItemAt(1);
        clickPreferenceListAt(0);
        replacePreferenceValue("1.234");
        clickText("OK");

        checkSummaryAtPreference("1.234", 0);
    }

    @Test
    public void restoreSettingsSetDefaultValues() {
        openDrawer();
        clickDrawerMenu("Settings");
        clickSettingsListItemAt(2);
        clickPreferenceListAt(0);
        pickPreferenceOption("Taryfa dwustrefowa (G12)");
        clickIcon("Restore default");
        clickText("Yes");

        checkSummaryAtPreference("Taryfa całodobowa (G11)", 0);
    }

    private void replacePreferenceValue(String text) {
        ViewInteraction editText = onView(
                allOf(withId(android.R.id.edit),
                        withParent(withClassName(is("android.widget.LinearLayout")))));
        editText.perform(scrollTo(), replaceText(text));
    }

    private void checkTitleAtPreference(String text, int position) {
        ViewInteraction title = onView(
                allOf(withId(android.R.id.title),
                        childAtPosition(
                                childAtPosition(
                                        childAtPosition(withId(android.R.id.list), position),
                                        1),
                                0)));
        title.check(matches(withText(text)));
    }

    private void checkSummaryAtPreference(String text, int position) {
        ViewInteraction summary = onView(
                allOf(withId(android.R.id.summary),
                        childAtPosition(
                                childAtPosition(
                                        childAtPosition(withId(android.R.id.list), position),
                                        1),
                                1)));
        summary.check(matches(withText(startsWith(text))));
    }

    private void clickText(String text) {
        onView(withText(text)).perform(click());
    }

    private void clickIcon(String description) {
        onView(withContentDescription(description)).perform(click());
    }

    private void pickPreferenceOption(String text) {
        ViewInteraction option = onView(
                allOf(withId(android.R.id.text1), withText(text)));
        option.perform(click());
    }

    private void clickPreferenceListAt(int position) {
        ViewInteraction preference = onView(childAtPosition(withId(android.R.id.list), position));
        preference.perform(click());
    }

    private void clickSettingsListItemAt(int position) {
        ViewInteraction settingsItem = onView(
                allOf(withId(R.id.settings_list_item),
                        childAtPosition(withId(R.id.list), position)));
        settingsItem.perform(click());
    }

    private void clickDrawerMenu(String label) {
        onView(allOf(withId(R.id.design_menu_item_text), withText(label)))
                .perform(click());
    }

    private void openDrawer() {
        clickIcon("Open navigation drawer");
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
