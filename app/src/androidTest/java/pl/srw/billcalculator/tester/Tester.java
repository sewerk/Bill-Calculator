package pl.srw.billcalculator.tester;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.tester.action.CustomClickAction;
import pl.srw.billcalculator.tester.interactor.RecyclerViewInteraction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
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

    public void changeOrientation(ActivityTestRule<DrawerActivity> testRule) {
        int current = testRule.getActivity().getRequestedOrientation();
        int change;
        if (current == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            change = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else if (current == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            change = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            testRule.getActivity().setRequestedOrientation(change);
            change = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            change = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        testRule.getActivity().setRequestedOrientation(change);
    }

    void openDrawer() {
        clickIcon(R.string.navigation_drawer_open);
    }

    void clickDrawerMenu(@StringRes int label) {
        onView(isAssignableFrom(NavigationView.class)).perform(swipeUp());
        onView(allOf(withId(R.id.design_menu_item_text), withText(label)))
                .perform(click());
    }

    void clickText(String text) {
        onView(withText(text)).perform(click());
    }

    void clickText(@StringRes int text) {
        onView(withText(text)).perform(click());
    }

    void typeInto(int viewId, String text) {
        onView(allOf(withId(viewId), isDisplayed())).perform(typeText(text), closeSoftKeyboard());
    }

    void clickIcon(@StringRes int description) {
        onView(withContentDescription(description)).perform(click());
    }

    void clickView(@IdRes int id) {
        onView(withId(id)).perform(click());
    }

    ViewAction swipeAwayRight() {
        return actionWithAssertions(new GeneralSwipeAction(Swipe.FAST,
                GeneralLocation.CENTER,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        float xy[] = GeneralLocation.CENTER_RIGHT.calculateCoordinates(view);
                        xy[0] += 20f * view.getWidth();
                        return xy;
                    }
                },
                Press.FINGER));
    }

    Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

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

    RecyclerViewInteraction onRecyclerView(Matcher<View> viewMatcher) {
        return new RecyclerViewInteraction(onView(viewMatcher));
    }

    private ViewAction click() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return actionWithAssertions(new CustomClickAction());
        }
        return ViewActions.click();
    }
}
