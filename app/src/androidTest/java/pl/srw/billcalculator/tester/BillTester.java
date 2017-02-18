package pl.srw.billcalculator.tester;

import android.support.annotation.IdRes;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.Matchers.allOf;

public class BillTester extends Tester {

    private AppTester parent;

    public BillTester(AppTester parent) {
        this.parent = parent;
    }

    public void checkTvMatch(@IdRes int tvId, String text) {
        onView(withId(tvId)).check(matches(withText(text)));
    }

    public void checkTvInRowMatch(@IdRes int tvId, @IdRes int parentId, String text) {
        onView(allOf(withId(tvId), withParent(withId(parentId)))).check(matches(withText(text)));
    }

    public void checkTvEndsWith(int tvId, String text) {
        onView(withId(tvId)).check(matches(withText(endsWith(text))));
    }

    public AppTester close() {
        pressBack();
        return parent;
    }
}
