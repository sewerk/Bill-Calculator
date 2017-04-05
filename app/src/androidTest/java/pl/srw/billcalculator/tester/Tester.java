package pl.srw.billcalculator.tester;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.DrawerActivity;

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

    void openDrawer() {
        clickIcon("Open navigation drawer");
    }

    void clickDrawerMenu(String label) {
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

    void clickIcon(String description) {
        onView(withContentDescription(description)).perform(click());
    }

    void clickView(@IdRes int id) {
        onView(withId(id)).perform(click());
    }

    static Matcher<View> childAtPosition(
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

    static Matcher<View> withImageDrawable(final int resourceId) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has image drawable resource " + resourceId);
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return sameBitmap(imageView.getContext(), imageView.getDrawable(), resourceId);
            }
        };
    }

    private static boolean sameBitmap(Context context, Drawable drawable, int resourceId) {
        Drawable otherDrawable = context.getResources().getDrawable(resourceId);
        if (drawable == null || otherDrawable == null) {
            return false;
        }
        if (drawable instanceof LayerDrawable && otherDrawable instanceof LayerDrawable) {
            drawable = ((LayerDrawable) drawable).getDrawable(1);
            otherDrawable = ((LayerDrawable) otherDrawable).getDrawable(1);
            return drawable.getConstantState().equals(otherDrawable.getConstantState());
        }
        if (drawable instanceof BitmapDrawable && otherDrawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap otherBitmap = ((BitmapDrawable) otherDrawable).getBitmap();
            return bitmap.sameAs(otherBitmap);
        }
        return false;
    }
}
