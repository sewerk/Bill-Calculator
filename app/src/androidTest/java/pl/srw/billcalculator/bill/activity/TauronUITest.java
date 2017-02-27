package pl.srw.billcalculator.bill.activity;

import org.threeten.bp.Month;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.tester.BillTester;
import pl.srw.billcalculator.tester.FormTester;
import pl.srw.billcalculator.tester.HistoryTester;
import pl.srw.billcalculator.tester.ProviderSettingsTester;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Created by kseweryn on 17.04.15.
 */
public class TauronUITest extends AbstractVerifyBillCreationUITest {

    private static final String READING_FROM = "7869";
    private static final String READING_TO = "8681";

    @Override
    protected Provider getProvider() {
        return Provider.TAURON;
    }

    @Override
    protected void changePrices(ProviderSettingsTester<FormTester> settingsTester) {
        settingsTester.getPreferenceAtLine(0).pickOption("Taryfa całodobowa (G11)");
        settingsTester.getPreferenceWithTitle(R.string.tauron_energia_elektryczna).changeValueTo("0.25470");
        settingsTester.getPreferenceWithTitle(R.string.tauron_oplata_dyst_zmienna).changeValueTo("0.1867");
        settingsTester.getPreferenceWithTitle(R.string.tauron_oplata_dyst_stala).changeValueTo("1.46");
        settingsTester.getPreferenceWithTitle(R.string.tauron_oplata_przejsciowa).changeValueTo("2.44");
        settingsTester.getPreferenceWithTitle(R.string.tauron_oplata_abonamentowa).changeValueTo("0.80");
    }

    @Override
    protected BillTester inputFormValuesAndCalculate(FormTester formTester) {
        return formTester
                .putIntoReadingFrom(READING_FROM)
                .putIntoReadingTo(READING_TO)
                .openDateFromPicker()
                .pickDate(30, Month.JULY, 2014)
                .acceptDate()
                .openDateToPicker()
                .pickDate(31, Month.DECEMBER, 2014)
                .acceptDate()
                .calculate();
    }

    @Override
    protected void verifyCalculatedValues(BillTester billTester) {
        // verify grupa taryfowa
        onView(withId(R.id.tv_tariff)).check(matches(withText(containsString("Grupa taryfowa Sprzedawcy: G11"))));
        // verify Okres rozliczeniowy
        billTester.checkTvMatch(R.id.tv_for_period, "Okres rozliczeniowy: 30.07.2014 - 31.12.2014");
        // verify Wskazanie poprzednie
        billTester.checkTvInRowMatch(R.id.tv_prev_date, R.id.row_za_energie_czynna, "30.07.2014");
        billTester.checkTvInRowMatch(R.id.tv_prev_reading, R.id.row_za_energie_czynna, READING_FROM);
        // verify Wskazanie obecne
        billTester.checkTvInRowMatch(R.id.tv_curr_date, R.id.row_za_energie_czynna, "31.12.2014");
        billTester.checkTvInRowMatch(R.id.tv_curr_reading, R.id.row_za_energie_czynna, READING_TO);
        // verify zużycie
        billTester.checkTvInRowMatch(R.id.tv_consumption, R.id.row_za_energie_czynna, "812");
        // verify il.m-c
        billTester.checkTvInRowMatch(R.id.tv_count, R.id.row_za_energie_czynna, "1");
        billTester.checkTvInRowMatch(R.id.tv_count, R.id.row_oplata_abonamentowa, "5");

        // verify ceny netto
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_za_energie_czynna, "0.25470");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_dyst_zm, "0.18670");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_dyst_stala, "1.46000");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_przejsciowa, "2.44000");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_abonamentowa, "0.80000");
        // verify warotść netto
        billTester.checkTvInRowMatch(R.id.tv_amount, R.id.row_za_energie_czynna, "206.82");
        billTester.checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_dyst_zm, "151.60");//34,87
        billTester.checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_dyst_stala, "7.30");//1,68
        billTester.checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_przejsciowa, "12.20");//2,81
        billTester.checkTvInRowMatch(R.id.tv_amount, R.id.row_oplata_abonamentowa, "4.00");//0,92
        // verify Razem
        billTester.checkTvMatch(R.id.tv_total_consumption, "812");
        billTester.checkTvMatch(R.id.tv_component_net_amount, "381.92");

        // verify podsumowanie zużycia
        billTester.checkTvMatch(R.id.tv_sell_net, "206.82");
        billTester.checkTvMatch(R.id.tv_sell_vat, "47.57");
        billTester.checkTvMatch(R.id.tv_sell_gross, "254.39");
        billTester.checkTvMatch(R.id.tv_distribute_net, "175.10");
        billTester.checkTvMatch(R.id.tv_distribute_vat, "40.27");
        billTester.checkTvMatch(R.id.tv_distribute_gross, "215.37");

        billTester.checkTvMatch(R.id.tv_total_net_amount, "381.92");
        billTester.checkTvMatch(R.id.tv_total_vat_amount, "87.84");
        billTester.checkTvMatch(R.id.tv_total_gross_amount, "469.76");

        // verify należność
        billTester.checkTvMatch(R.id.tv_total_amount, "469.76");
        // verify akcyza
        billTester.checkTvEndsWith(R.id.tv_excise, "16.24 zł.");
    }

    @Override
    protected void verifyAndOpenBillFromHistory(HistoryTester historyTester) {
        historyTester.openBillWithReadings(READING_FROM, READING_TO);
    }
}
