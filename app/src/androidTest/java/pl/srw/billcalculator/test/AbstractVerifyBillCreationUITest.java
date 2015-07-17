package pl.srw.billcalculator.test;

import android.support.annotation.StringRes;

import org.junit.Before;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.testutils.HistoryGenerator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.srw.billcalculator.testutils.EspressoHelper.waitForUi;

/**
 * Created by kseweryn on 15.07.15.
 */
public abstract class AbstractVerifyBillCreationUITest {

    @Before
    public void setUp() throws Exception {
        GeneralPreferences.markFirstLaunch();
        HistoryGenerator.clear();
    }

    abstract void testBillCalculationWithStorage() throws Exception;

    protected void setPricesInSettings(@StringRes int providerLabelResId) {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withText(providerLabelResId)).perform(click());
        changePrices();
        pressBack();
        pressBack();
    }

    protected abstract void changePrices();

    protected void changePrice(int textRes, String input) {
        onView(withText(textRes)).perform(click());
        waitForUi();
        onView(withId(android.R.id.edit)).perform(clearText(), typeText(input), closeSoftKeyboard());
        waitForUi();
        onView(withId(android.R.id.button1)).perform(click());
    }

    protected void revertPricesToDefault(@StringRes int providerResId) {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withText(providerResId)).perform(click());

        onView(withId(R.id.action_default)).perform(click());
        onView(withText(R.string.restore_prices_confirm)).perform(click());

        pressBack();
        pressBack();
    }

}
