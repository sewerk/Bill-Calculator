package pl.srw.billcalculator.test;

import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.threeten.bp.Month;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.MainActivity;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static pl.srw.billcalculator.testutils.EspressoHelper.checkTvInRowMatch;
import static pl.srw.billcalculator.testutils.EspressoHelper.checkTvMatch;
import static pl.srw.billcalculator.testutils.EspressoHelper.onDatePickerSet;
import static pl.srw.billcalculator.testutils.EspressoHelper.waitForUi;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigUITest extends AbstractVerifyBillCreationUITest {

    @Rule
    public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, true);

    @Override
    @Test
    @LargeTest
    public void testBillCalculationWithStorage() throws Exception {
        setPricesInSettings(R.string.pgnig_prices);
        inputFormValuesAndCalculate();
        verifyCalculatedValues();
        revertPricesToDefault(R.string.pgnig_prices);
        verifyBillInHistory();
        verifyCalculatedValues();
    }

    @Override
    protected void changePrices() {
        changePrice(R.string.wspolczynnik_konwersji, "11.094");
        changePrice(R.string.paliwo_gazowe, "0.11815");
        changePrice(R.string.settings_oplata_abonamentowa, "6.97");
        changePrice(R.string.dystrybucyjna_stala, "22.62");
        changePrice(R.string.dystrybucyjna_zmienna, "0.03823");
    }

    private void inputFormValuesAndCalculate() {
        onView(withText(Provider.PGNIG.toString())).perform(click());
        onView(allOf(withId(R.id.et_reading_from), isDisplayed())).perform(typeText("6696"));
        onView(allOf(withId(R.id.et_reading_to), isDisplayed())).perform(typeText("7101"), closeSoftKeyboard());
        waitForUi();

        onDatePickerSet(R.id.button_date_from, 10, Month.OCTOBER, 2014);
        onDatePickerSet(R.id.button_date_to, 15, Month.DECEMBER, 2014);

        onView(allOf(withId(R.id.button_calculate), isDisplayed())).perform(click());
    }

    private void verifyCalculatedValues() {
        checkTvMatch(R.id.tv_prev_reading_date, "10.10.2014");
        checkTvMatch(R.id.tv_curr_reading_date, "15.12.2014");
        // verify odczyty
        checkTvMatch(R.id.tv_previous_reading, "6696 [m³]");
        checkTvMatch(R.id.tv_current_reading, "7101 [m³]");
        // verify zużycie 405 m3
        checkTvMatch(R.id.tv_consumption, "405 [m³]");
        checkTvMatch(R.id.tv_total_consumption, "Zużycie razem: 405 [m³]");
        // verify współ.konw
        checkTvMatch(R.id.tv_conversion_factor, "Wsp. konwersji: 11.094");
        // verify ilość 4493 kWh
        checkTvMatch(R.id.tv_total_consumption_kWh, "Zużycie razem: 4493 [kWh]");
        //verify za okres
        checkTvInRowMatch(R.id.tv_date_from, R.id.row_abonamentowa, "10.10.2014");
        checkTvInRowMatch(R.id.tv_date_to, R.id.row_abonamentowa, "15.12.2014");
        // verify ilość
        checkTvInRowMatch(R.id.tv_count, R.id.row_abonamentowa, "2.000");
        checkTvInRowMatch(R.id.tv_count, R.id.row_paliwo_gazowe, "4493");

        // verify cenny netto
        checkTvInRowMatch(R.id.tv_net_price, R.id.row_abonamentowa, "6.97000");
        checkTvInRowMatch(R.id.tv_net_price, R.id.row_paliwo_gazowe, "0.11815");
        checkTvInRowMatch(R.id.tv_net_price, R.id.row_dystrybucyjna_stala, "22.62000");
        checkTvInRowMatch(R.id.tv_net_price, R.id.row_dystrybucyjna_zmienna, "0.03823");
        // verify warotść netto 530.85 13,94 45,24 171,77
        checkTvInRowMatch(R.id.tv_net_charge, R.id.row_abonamentowa, "13.94");
        checkTvInRowMatch(R.id.tv_net_charge, R.id.row_paliwo_gazowe, "530.85");
        checkTvInRowMatch(R.id.tv_net_charge, R.id.row_dystrybucyjna_stala, "45.24");
        checkTvInRowMatch(R.id.tv_net_charge, R.id.row_dystrybucyjna_zmienna, "171.77");

        // VAT 122,10 3,21 10,41 39,51
        // wartość brutto 652,95 17,15 55,65 211,28
        // verify Razem 761,80 175,23 937,03
        checkTvInRowMatch(R.id.tv_net_charge, R.id.row_sum, "761.80");
        checkTvInRowMatch(R.id.tv_vat_amount, R.id.row_sum, "175.23");
        checkTvInRowMatch(R.id.tv_gross_charge, R.id.row_sum, "937.03");
        checkTvMatch(R.id.tv_invoice_value, "Wartość faktury brutto: 937.03 zł");

        pressBack();
    }

    private void verifyBillInHistory() {
        onView(withId(R.id.action_history)).perform(click());
        onView(withText("6696 - 7101")).perform(click());
    }
}
