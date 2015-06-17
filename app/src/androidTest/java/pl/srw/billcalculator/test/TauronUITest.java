package pl.srw.billcalculator.test;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.threeten.bp.Month;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.MainActivity;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.testutils.HistoryGenerator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kseweryn on 17.04.15.
 */
@LargeTest
public class TauronUITest {

    @Rule public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        GeneralPreferences.markFirstLaunch();
        HistoryGenerator.clear();
    }

    @Test
    public void testBillCalculationWithStorage() throws Exception {
        activityTestRule.getActivity();
        setPricesInSettings();
        inputFormValuesAndCalculate();
        verifyCalculatedValues();
        revertPricesToDefault();
        verifyBillInHistory();
    }

    private void setPricesInSettings() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withText(R.string.tauron_prices)).perform(click());
        changePrices();
        pressBack();
        pressBack();
    }

    private void changePrices() {
        changePrice(R.string.tauron_energia_elektryczna, "0.25470");
        changePrice(R.string.tauron_oplata_dyst_zmienna, "0.1867");
        changePrice(R.string.tauron_oplata_dyst_stala, "1.46");
        changePrice(R.string.tauron_oplata_przejsciowa, "2.44");
        changePrice(R.string.tauron_oplata_abonamentowa, "0.80");
    }

    private void changePrice(int textRes, String input) {
        onView(withText(textRes)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(clearText(), typeText(input), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());//TODO: failing sometimes
    }

    private void inputFormValuesAndCalculate() {
        onView(withId(R.id.et_reading_from)).perform(typeText("7869"));
        onView(withId(R.id.et_reading_to)).perform(typeText("8681"));
        onView(withId(R.id.button_date_from)).perform(click());
        PickerActions.setDate(2014, Month.JULY.getValue()-1, 30);//TODO: check how to use
        onView(withId(R.id.button_date_to)).perform(click());
        PickerActions.setDate(2014, Month.DECEMBER.getValue()-1, 31);
    }

    private void verifyCalculatedValues() {
        //TODO: implement
    }

    private void revertPricesToDefault() {

    }

    private void verifyBillInHistory() {
        
    }
}
