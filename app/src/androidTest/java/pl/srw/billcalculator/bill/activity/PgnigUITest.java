package pl.srw.billcalculator.bill.activity;

import org.threeten.bp.Month;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.tester.BillTester;
import pl.srw.billcalculator.tester.FormTester;
import pl.srw.billcalculator.tester.HistoryTester;
import pl.srw.billcalculator.tester.ProviderSettingsTester;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigUITest extends AbstractVerifyBillCreationUITest {

    private static final String READING_FROM = "6696";
    private static final String READING_TO = "7101";

    @Override
    protected Provider getProvider() {
        return Provider.PGNIG;
    }

    @Override
    protected void changePrices(ProviderSettingsTester settingsTester) {
        settingsTester.getPreferenceWithTitle(R.string.wspolczynnik_konwersji).changeValueTo("11.094");
        settingsTester.getPreferenceWithTitle(R.string.paliwo_gazowe).changeValueTo("0.11815");
        settingsTester.getPreferenceWithTitle(R.string.settings_oplata_abonamentowa).changeValueTo("6.97");
        settingsTester.getPreferenceWithTitle(R.string.dystrybucyjna_stala).changeValueTo("22.62");
        settingsTester.getPreferenceWithTitle(R.string.dystrybucyjna_zmienna).changeValueTo("0.03823");
    }

    @Override
    protected BillTester inputFormValuesAndCalculate(FormTester formTester) {
        return formTester
                .putIntoReadingFrom(READING_FROM)
                .putIntoReadingTo(READING_TO)
                .openDateFromPicker()
                .pickDate(10, Month.OCTOBER, 2014)
                .acceptDate()
                .openDateToPicker()
                .pickDate(15, Month.DECEMBER, 2014)
                .acceptDate()
                .calculate();
    }

    @Override
    protected void verifyCalculatedValues(BillTester billTester) {
        billTester.checkTvMatch(R.id.tv_prev_reading_date, "10.10.2014");
        billTester.checkTvMatch(R.id.tv_curr_reading_date, "15.12.2014");
        // verify odczyty
        billTester.checkTvMatch(R.id.tv_previous_reading, READING_FROM + " [m³]");
        billTester.checkTvMatch(R.id.tv_current_reading, READING_TO + " [m³]");
        // verify zużycie 405 m3
        billTester.checkTvMatch(R.id.tv_consumption, "405 [m³]");
        billTester.checkTvMatch(R.id.tv_total_consumption, "Zużycie razem: 405 [m³]");
        // verify współ.konw
        billTester.checkTvMatch(R.id.tv_conversion_factor, "Wsp. konwersji: 11.094");
        // verify ilość 4493 kWh
        billTester.checkTvMatch(R.id.tv_total_consumption_kWh, "Zużycie razem: 4493 [kWh]");
        //verify za okres
        billTester.checkTvInRowMatch(R.id.tv_date_from, R.id.row_abonamentowa, "10.10.2014");
        billTester.checkTvInRowMatch(R.id.tv_date_to, R.id.row_abonamentowa, "15.12.2014");
        // verify ilość
        billTester.checkTvInRowMatch(R.id.tv_count, R.id.row_abonamentowa, "2.0000");
        billTester.checkTvInRowMatch(R.id.tv_count, R.id.row_dystrybucyjna_stala, "2.1613");
        billTester.checkTvInRowMatch(R.id.tv_count, R.id.row_paliwo_gazowe, "4493");

        // verify cenny netto
        billTester.checkTvInRowMatch(R.id.tv_net_price, R.id.row_abonamentowa, "6.97000");
        billTester.checkTvInRowMatch(R.id.tv_net_price, R.id.row_paliwo_gazowe, "0.11815");
        billTester.checkTvInRowMatch(R.id.tv_net_price, R.id.row_dystrybucyjna_stala, "22.62000");
        billTester.checkTvInRowMatch(R.id.tv_net_price, R.id.row_dystrybucyjna_zmienna, "0.03823");
        // verify warotść netto
        billTester.checkTvInRowMatch(R.id.tv_net_charge, R.id.row_abonamentowa, "13.94");
        billTester.checkTvInRowMatch(R.id.tv_net_charge, R.id.row_paliwo_gazowe, "530.85");
        billTester.checkTvInRowMatch(R.id.tv_net_charge, R.id.row_dystrybucyjna_stala, "48.89");
        billTester.checkTvInRowMatch(R.id.tv_net_charge, R.id.row_dystrybucyjna_zmienna, "171.77");

        // VAT 122,10 3,21 11,24 39,51
        // verify Razem
        billTester.checkTvInRowMatch(R.id.tv_net_charge, R.id.row_sum, "765.45");
        billTester.checkTvInRowMatch(R.id.tv_vat_amount, R.id.row_sum, "176.06");
        billTester.checkTvInRowMatch(R.id.tv_gross_charge, R.id.row_sum, "941.51");
        billTester.checkTvMatch(R.id.tv_invoice_value, "Wartość faktury brutto: 941.51 zł");
    }

    @Override
    protected void verifyAndOpenBillFromHistory(HistoryTester historyTester) {
        historyTester.openBillWithReadings(READING_FROM, READING_TO);
    }

    @Override
    protected void removeBillFromHistory(HistoryTester historyTester) {
        historyTester.removeBillWithReadings(READING_FROM, READING_TO);
    }
}
