package pl.srw.billcalculator.tester;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import pl.srw.billcalculator.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

abstract class Tester {

    protected void openDrawer() {
        clickIcon("Open navigation drawer");
    }

    protected void clickDrawerMenu(String label) {
        onView(isAssignableFrom(NavigationView.class)).perform(swipeUp());
        onView(allOf(withId(R.id.design_menu_item_text), withText(label)))
                .perform(click());
    }

    protected void clickText(String text) {
        onView(withText(text)).perform(click());
    }

    protected void clickText(@StringRes int text) {
        onView(withText(text)).perform(click());
    }

    protected void typeInto(int viewId, String text) {
        onView(allOf(withId(viewId), isDisplayed())).perform(typeText(text), closeSoftKeyboard());
    }

    protected void clickIcon(String description) {
        onView(withContentDescription(description)).perform(click());
    }

    protected void clickView(@IdRes int id) {
        onView(withId(id)).perform(click());
    }

    protected static Matcher<View> childAtPosition(
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
