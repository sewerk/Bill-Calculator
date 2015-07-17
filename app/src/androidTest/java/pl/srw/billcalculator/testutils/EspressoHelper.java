package pl.srw.billcalculator.testutils;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.threeten.bp.Month;

import pl.srw.billcalculator.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by kseweryn on 16.07.15.
 */
public final class EspressoHelper {

    private EspressoHelper() {}

    public static void waitForUi() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void onDatePickerSet(int dateViewId, int dayOfMonth, Month monthOfYear, int year) {
        onView(allOf(withId(dateViewId), isDisplayed())).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(year, monthOfYear.getValue(), dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void checkTvMatch(@IdRes int tvId, String text) {
        onView(withId(tvId)).check(matches(withText(text)));
    }

    public static void checkTvInRowMatch(@IdRes int tvId, @IdRes int parentId, String text) {
        onView(allOf(withId(tvId), withParent(withId(parentId)))).check(matches(withText(text)));
    }

    public static void checkImageInRowHasTag(String text, @DrawableRes int drawableTag) {
        onView(Matchers.allOf(withId(R.id.iv_bill_type), hasSibling(withChild(withText(text))))).check(matches(withTagValue(is((Object) drawableTag))));
    }
}
