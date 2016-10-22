package pl.srw.billcalculator.test;

import android.support.test.espresso.intent.matcher.ComponentNameMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.activity.PgeBillActivity;
import pl.srw.billcalculator.bill.service.PgeBillStoringService;
import pl.srw.billcalculator.form.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static pl.srw.billcalculator.testutils.EspressoHelper.waitForUi;

/**
 * Created by kseweryn on 20.07.15.
 */
public class MainFormUITest {

    @Rule
    public final IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class, true);

    //FIXME    @Test
    public void shouldStartServiceOnCalculateClicked() {
        onView(allOf(withId(R.id.et_reading_from), isDisplayed())).perform(typeText("123"));
        onView(allOf(withId(R.id.et_reading_to), isDisplayed())).perform(typeText("124"), closeSoftKeyboard());
        waitForUi();
        onView(allOf(withId(R.id.button_calculate), isDisplayed())).perform(click());
        intended(hasComponent(ComponentNameMatchers.hasClassName(PgeBillActivity.class.getName())));
        intended(hasComponent(ComponentNameMatchers.hasClassName(PgeBillStoringService.class.getName())));
    }
}
