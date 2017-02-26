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

public class PgeUITest extends AbstractVerifyBillCreationUITest {

    private static final String READING_DAY_FROM = "16683";
    private static final String READING_DAY_TO = "16787";
    private static final String READING_NIGHT_FROM = "16787";
    private static final String READING_NIGHT_TO = "16798";

    @Override
    protected Provider getProvider() {
        return Provider.PGE;
    }

    @Override
    protected void changePrices(ProviderSettingsTester<FormTester> settingsTester) {
        settingsTester.getPreferenceAtLine(0).pickOption("Taryfa dwustrefowa (G12)");
        settingsTester.getPreferenceWithTitle(R.string.za_energie_czynna_G12dzien).changeValueTo("0.2706");
        settingsTester.getPreferenceWithTitle(R.string.oplata_sieciowa_G12dzien).changeValueTo("0.2177");
        settingsTester.getPreferenceWithTitle(R.string.skladnik_jakosciowy).changeValueTo("0.0084");
        settingsTester.getPreferenceWithTitle(R.string.za_energie_czynna_G12noc).changeValueTo("0.2539");
        settingsTester.getPreferenceWithTitle(R.string.oplata_sieciowa_G12noc).changeValueTo("0.2192");
        settingsTester.getPreferenceWithTitle(R.string.oplata_przejsciowa).changeValueTo("0.77");
        settingsTester.getPreferenceWithTitle(R.string.oplata_stala_za_przesyl).changeValueTo("1.78");
        settingsTester.getPreferenceWithTitle(R.string.oplata_abonamentowa).changeValueTo("5.31");
    }

    @Override
    protected BillTester inputFormValuesAndCalculate(FormTester formTester) {
        return formTester
                .putIntoReadingDayFrom(READING_DAY_FROM)
                .putIntoReadingDayTo(READING_DAY_TO)
                .putIntoReadingNightFrom(READING_NIGHT_FROM)
                .putIntoReadingNightTo(READING_NIGHT_TO)
                .openDateFromPicker()
                .pickDate(5, Month.DECEMBER, 2013)
                .acceptDate()
                .openDateToPicker()
                .pickDate(4, Month.JANUARY, 2014)
                .acceptDate()
                .calculate();
    }

    @Override
    protected void verifyCalculatedValues(BillTester billTester) {
        // verify grupa taryfowa
        onView(withId(R.id.tv_tariff)).check(matches(withText("Taryfa: G12")));
        // verify Okres rozliczeniowy
        billTester.checkTvMatch(R.id.tv_for_period, "Za okres od 05/12/2013 do 04/01/2014");
        // verify Wskazanie poprzednie
        billTester.checkTvInRowMatch(R.id.tv_previous_reading, R.id.row_za_energie_czynna, READING_DAY_FROM);
        billTester.checkTvInRowMatch(R.id.tv_previous_reading, R.id.row_za_energie_czynna2, READING_NIGHT_FROM);
        // verify Wskazanie obecne
        billTester.checkTvInRowMatch(R.id.tv_current_reading, R.id.row_za_energie_czynna, READING_DAY_TO);
        billTester.checkTvInRowMatch(R.id.tv_current_reading, R.id.row_za_energie_czynna2, READING_NIGHT_TO);
        // verify zużycie
        billTester.checkTvInRowMatch(R.id.tv_count, R.id.row_za_energie_czynna, "104");
        billTester.checkTvInRowMatch(R.id.tv_count, R.id.row_za_energie_czynna2, "11");
        // verify il.m-c
        billTester.checkTvInRowMatch(R.id.tv_month_count, R.id.row_za_energie_czynna, "");
        billTester.checkTvInRowMatch(R.id.tv_month_count, R.id.row_za_energie_czynna2, "");
        billTester.checkTvInRowMatch(R.id.tv_month_count, R.id.row_oplata_abonamentowa, "1.00");

        // verify ceny netto
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_za_energie_czynna, "0.2706");//28,1424
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_skladnik_jakosciowy, "0.0084");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_sieciowa, "0.2177");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_za_energie_czynna2, "0.2539");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_skladnik_jakosciowy2, "0.0084");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_sieciowa2, "0.2192");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_przejsciowa, "0.7700");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_stala_za_przesyl, "1.7800");
        billTester.checkTvInRowMatch(R.id.tv_price, R.id.row_oplata_abonamentowa, "5.3100");
        // verify warotść netto
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_za_energie_czynna, "28.14");//6,472752
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_skladnik_jakosciowy, "0.87");//0,200928
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_oplata_sieciowa, "22.64");//5,207384
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_za_energie_czynna2, "2.79");//0,642367
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_skladnik_jakosciowy2, "0.09");//0,021252
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_oplata_sieciowa2, "2.41");//0,554576
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_oplata_przejsciowa, "0.77");//0,1771
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_oplata_stala_za_przesyl, "1.78");//0,4094
        billTester.checkTvInRowMatch(R.id.tv_charge, R.id.row_oplata_abonamentowa, "5.31");//1,2213
        // verify Ogółem
        billTester.checkTvMatch(R.id.tv_total_net_charge, "64.80");
        // verify akcyza
        billTester.checkTvEndsWith(R.id.tv_excise, "2.30 zł.");

        // verify podsumowanie zużycia
        billTester.checkTvMatch(R.id.tv_net_charge, "64.80");
        billTester.checkTvMatch(R.id.tv_vat_amount, "14.91");
        billTester.checkTvMatch(R.id.tv_gross_charge, "79.71");

        // verify Do zapłaty
        billTester.checkTvEndsWith(R.id.tv_to_pay, "79.71 zł");
    }

    @Override
    protected void verifyAndOpenBillFromHistory(HistoryTester historyTester) {
        historyTester.openBillWithReadings(READING_DAY_FROM, READING_DAY_TO);
    }

    @Override
    protected void removeBillFromHistory(HistoryTester historyTester) {
        historyTester.removeBillWithReadings(READING_DAY_FROM, READING_DAY_TO);
    }
}
