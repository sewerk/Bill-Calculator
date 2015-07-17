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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static pl.srw.billcalculator.testutils.EspressoHelper.checkTvInRowMatch;
import static pl.srw.billcalculator.testutils.EspressoHelper.checkTvMatch;
import static pl.srw.billcalculator.testutils.EspressoHelper.onDatePickerSet;
import static pl.srw.billcalculator.testutils.EspressoHelper.waitForUi;

/**
 * Created by kseweryn on 17.04.15.
 */
public class TauronUITest extends AbstractVerifyBillCreationUITest {

    @Rule public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, true);

    @Override
    @LargeTest
    @Test
    public void testBillCalculationWithStorage() throws Exception {
        setPricesInSettings(R.string.tauron_prices);
        inputFormValuesAndCalculate();
        verifyCalculatedValues();
        revertPricesToDefault(R.string.tauron_prices);
        verifyBillInHistory();
        verifyCalculatedValues();
    }

    @Override
    protected void changePrices() {
        changePrice(R.string.tauron_energia_elektryczna, "0.25470");
        changePrice(R.string.tauron_oplata_dyst_zmienna, "0.1867");
        changePrice(R.string.tauron_oplata_dyst_stala, "1.46");
        changePrice(R.string.tauron_oplata_przejsciowa, "2.44");
        changePrice(R.string.tauron_oplata_abonamentowa, "0.80");
    }

    private void inputFormValuesAndCalculate() {
        onView(withText(Provider.TAURON.toString())).perform(click());
        onView(allOf(withId(R.id.et_reading_from), isDisplayed())).perform(typeText("7869"));
        onView(allOf(withId(R.id.et_reading_to), isDisplayed())).perform(typeText("8681"), closeSoftKeyboard());
        waitForUi();

        onDatePickerSet(R.id.button_date_from, 30, Month.JULY, 2014);
        onDatePickerSet(R.id.button_date_to, 31, Month.DECEMBER, 2014);

        onView(allOf(withId(R.id.button_calculate), isDisplayed())).perform(click());
    }

    private void verifyCalculatedValues() {
        // verify grupa taryfowa
        onView(withId(R.id.tv_tariff)).check(matches(withText(containsString("Grupa taryfowa Sprzedawcy: G11"))));
        // verify Okres rozliczeniowy
        checkTvMatch(R.id.tv_for_period, "Okres rozliczeniowy: 30.07.2014 - 31.12.2014");
        // verify Wskazanie poprzednie
        checkTvInRowMatch(R.id.tv_prev_date, R.id.row_za_energie_czynna, "30.07.2014");
        checkTvInRowMatch(R.id.tv_prev_reading, R.id.row_za_energie_czynna, "7869");
        // verify Wskazanie obecne
        checkTvInRowMatch(R.id.tv_curr_date, R.id.row_za_energie_czynna, "31.12.2014");
        checkTvInRowMatch(R.id.tv_curr_reading, R.id.row_za_energie_czynna, "8681");
        // verify zużycie
        checkTvInRowMatch(R.id.tv_consumption, R.id.row_za_energie_czynna, "812");
        // verify il.m-c
        checkTvInRowMatch(R.id.tv_count, R.id.row_za_energie_czynna, "1");
        checkTvInRowMatch(R.id.tv_count, R.id.row_oplata_abonamentowa, "5");

        // verify ceny netto
        checkTvInRowMatch(R.id.tv_price, R.id.row_za_energie_czynna, "0.25470");
        checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_dyst_zm, "0.18670");
        checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_dyst_stala, "1.46000");
        checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_przejsciowa, "2.44000");
        checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_abonamentowa, "0.80000");
        // verify warotść netto
        checkTvInRowMatch(R.id.tv_amount, R.id.row_za_energie_czynna, "206.82");
        checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_dyst_zm, "151.60");//34,87
        checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_dyst_stala, "7.30");//1,68
        checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_przejsciowa, "12.20");//2,81
        checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_abonamentowa, "4.00");//0,92
        // verify Razem
        checkTvMatch(R.id.tv_total_consumption, "812");
        checkTvMatch(R.id.tv_component_net_amount, "381.92");

        // verify podsumowanie zużycia
        checkTvMatch(R.id.tv_sell_net, "206.82");
        checkTvMatch(R.id.tv_sell_vat, "47.57");
        checkTvMatch(R.id.tv_sell_gross, "254.39");
        checkTvMatch(R.id.tv_distribute_net, "175.10");
        checkTvMatch(R.id.tv_distribute_vat, "40.27");
        checkTvMatch(R.id.tv_distribute_gross, "215.37");

        checkTvMatch(R.id.tv_total_net_amount, "381.92");
        checkTvMatch(R.id.tv_total_vat_amount, "87.84");
        checkTvMatch(R.id.tv_total_gross_amount, "469.76");

        // verify należność
        checkTvMatch(R.id.tv_total_amount, "469.76");
        // verify akcyza
        onView(withId(R.id.tv_excise)).check(matches(withText(endsWith("16.24 zł."))));

        pressBack();
    }

    private void verifyBillInHistory() {
        onView(withId(R.id.action_history)).perform(click());
        onView(withText("7869 - 8681")).perform(click());
    }

}
