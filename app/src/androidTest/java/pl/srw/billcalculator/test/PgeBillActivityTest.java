package pl.srw.billcalculator.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigDecimal;

import pl.srw.billcalculator.PgeBillActivity;
import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.pojo.PgePrices;

/**
 * Created by Kamil Seweryn.
 */
public class PgeBillActivityTest extends ActivityInstrumentationTestCase2<PgeBillActivity> {

    public static final String DATE_FROM = "01/12/2014";
    public static final String DATE_TO = "31/12/2014";
    public static final int FROM = 12;
    public static final int TO = 25;

    private PgeBillActivity sut;
    private Context context;
    private PgePrices pgePrices;

    public PgeBillActivityTest() {
        super(PgeBillActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        pgePrices = new PgePrices(preferences);
        setDummyIntent();
        setDefaultPrices();
    }

    public void testTaryfaLabelG11DependOnIntentExtra() {
        Intent intent = new Intent();
        putReadings(intent, FROM, TO);
        setActivityIntent(intent);

        sut = getActivity();
        String expected = sut.getString(R.string.pge_tariff_G11_on_bill);
        assEqText(expected, R.id.tv_tariff);
    }

    public void testTaryfaLabelG12DependOnIntentExtra() {
        Intent intent = new Intent();
        putReadings(intent, FROM, TO, FROM, TO);
        setActivityIntent(intent);

        sut = getActivity();
        String expected = sut.getString(R.string.pge_tariff_G12_on_bill);
        assEqText(expected, R.id.tv_tariff);
    }

    public void testZaOkresLabelDependsOnIntentExtra() {
        final String dateFrom = "03/12/2014";
        final String dateTo = "29/12/2014";

        Intent intent = new Intent();
        putReadings(intent, FROM, TO);
        putDates(intent, dateFrom, dateTo);
        setActivityIntent(intent);

        sut = getActivity();
        String expected = sut.getString(R.string.for_period, dateFrom, dateTo);
        assEqText(expected, R.id.tv_for_period);
    }

    public void testWskazaniaG11DependsOnIntentExtra() {
        final int from = 122;
        final int to = 213;
        final int diff = to - from;

        Intent intent = new Intent();
        putReadings(intent, from, to);
        setActivityIntent(intent);

        sut = getActivity();
        checkReadingsValuesInRow(from, to, diff, R.id.row_za_energie_czynna);
        checkReadingsValuesInRow(from, to, diff, R.id.row_skladnik_jakosciowy);
        checkReadingsValuesInRow(from, to, diff, R.id.row_oplata_sieciowa);

        String expected = sut.getString(R.string.akcyza, diff, (diff * 0.02));
        assEqText(expected, R.id.tv_excise);
    }

    public void testWskazaniaG12DependsOnIntentExtra() {
        final int dayFrom = 1722;
        final int dayTo = 2193;
        final int dayDiff = dayTo - dayFrom;
        final int nightFrom = 234;
        final int nightTo = 532;
        final int nightDiff = nightTo - nightFrom;
        final int diff = dayDiff + nightDiff;

        Intent intent = new Intent();
        putReadings(intent, dayFrom, dayTo, nightFrom, nightTo);
        setActivityIntent(intent);

        sut = getActivity();
        checkReadingsValuesInRow(dayFrom, dayTo, dayDiff, R.id.row_za_energie_czynna);
        checkReadingsValuesInRow(dayFrom, dayTo, dayDiff, R.id.row_skladnik_jakosciowy);
        checkReadingsValuesInRow(dayFrom, dayTo, dayDiff, R.id.row_oplata_sieciowa);

        checkReadingsValuesInRow(nightFrom, nightTo, nightDiff, R.id.row_za_energie_czynna2);
        checkReadingsValuesInRow(nightFrom, nightTo, nightDiff, R.id.row_skladnik_jakosciowy2);
        checkReadingsValuesInRow(nightFrom, nightTo, nightDiff, R.id.row_oplata_sieciowa2);

        String expected = sut.getString(R.string.akcyza, diff, (diff * 0.02));
        assEqText(expected, R.id.tv_excise);
    }

    public void testCalculationForG11Readings() {
        final int ilosc = TO - FROM;
        Intent intent = new Intent();
        putReadings(intent, FROM, TO);
        setActivityIntent(intent);

        sut = getActivity();
        checkNaleznoscInRow(pgePrices.getZaEnergieCzynna(), ilosc, R.id.row_za_energie_czynna);
        checkNaleznoscInRow(pgePrices.getSkladnikJakosciowy(), ilosc, R.id.row_skladnik_jakosciowy);
        checkNaleznoscInRow(pgePrices.getOplataSieciowa(), ilosc, R.id.row_oplata_sieciowa);
    }

    public void testSumCostForG11Readings() {
        final int ilosc = TO - FROM;
        Intent intent = new Intent();
        putReadings(intent, FROM, TO);
        setActivityIntent(intent);

        sut = getActivity();

        BigDecimal sumNetto = countSum(ilosc);
        String expected = sumNetto.toString();
        assEqText(expected, R.id.tv_total_net_charge);
        assEqText(expected, R.id.tv_net_charge);

        BigDecimal vatCost = sumNetto.multiply(new BigDecimal("0.23")).setScale(2, BigDecimal.ROUND_HALF_UP);
        assEqText(vatCost.toString(), R.id.tv_vat_amount);

        BigDecimal sumAll = sumNetto.add(vatCost);
        assEqText(sumAll.toString(), R.id.tv_gross_charge);

        String doZaplatyExpected = sut.getString(R.string.to_pay, sumAll);
        assEqText(doZaplatyExpected, R.id.tv_to_pay);
    }

    public void testCalculationForG12Readings() {
        Intent intent = new Intent();
        putReadings(intent, 0, 10, 0, 100);
        setActivityIntent(intent);

//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(context.getString(R.string.preferences_pge_za_energie_czynna_G12dzien), "1.01");
//        editor.putString(context.getString(R.string.preferences_pge_za_energie_czynna_G12noc), "2.02");
//        editor.putString(context.getString(R.string.preferences_pge_skladnik_jakosciowy), "3.03");
//        editor.putString(context.getString(R.string.preferences_pge_oplata_sieciowa_G12dzien), "4.04");
//        editor.putString(context.getString(R.string.preferences_pge_oplata_sieciowa_G12noc), "5.05");
//        editor.putString(context.getString(R.string.preferences_pge_oplata_przejsciowa), "6.06");
//        editor.putString(context.getString(R.string.preferences_pge_oplata_stala_za_przesyl), "7.07");
//        editor.putString(context.getString(R.string.preferences_pge_oplata_abonamentowa), "8.08");
//        editor.commit();TODO: preapre test migration of preferences
        pgePrices.setZaEnergieCzynnaDzien("1.01");
        pgePrices.setZaEnergieCzynnaNoc("2.02");
        pgePrices.setSkladnikJakosciowy("3.03");
        pgePrices.setOplataSieciowaDzien("4.04");
        pgePrices.setOplataSieciowaNoc("5.05");
        pgePrices.setOplataPrzejsciowa("6.06");
        pgePrices.setOplataStalaZaPrzesyl("7.07");
        pgePrices.setOplataAbonamentowa("8.08");

        sut = getActivity();
        assEqTextInRow("10.10", R.id.tv_charge, R.id.row_za_energie_czynna);
        assEqTextInRow("202.00", R.id.tv_charge, R.id.row_za_energie_czynna2);
        assEqTextInRow("30.30", R.id.tv_charge, R.id.row_skladnik_jakosciowy);
        assEqTextInRow("303.00", R.id.tv_charge, R.id.row_skladnik_jakosciowy2);
        assEqTextInRow("40.40", R.id.tv_charge, R.id.row_oplata_sieciowa);
        assEqTextInRow("505.00", R.id.tv_charge, R.id.row_oplata_sieciowa2);
        assEqTextInRow("6.06", R.id.tv_charge, R.id.row_oplata_przejsciowa);
        assEqTextInRow("7.07", R.id.tv_charge, R.id.row_oplata_stala_za_przesyl);
        assEqTextInRow("8.08", R.id.tv_charge, R.id.row_oplata_abonamentowa);

        assEqText("1112.01", R.id.tv_total_net_charge);
        assEqText("1112.01", R.id.tv_net_charge);
        assEqText("255.76", R.id.tv_vat_amount);
        assEqText("1367.77", R.id.tv_gross_charge);
    }

    // ================================================================ private methods
    private void checkReadingsValuesInRow(int expectedPoprzednie, int expectedBiezace, int expectedIlosc, int rowId) {
        assEqTextInRow("" + expectedBiezace, R.id.tv_current_reading, rowId);
        assEqTextInRow("" + expectedPoprzednie, R.id.tv_previous_reading, rowId);
        assEqTextInRow("" + expectedIlosc, R.id.tv_count, rowId);
    }

    private void checkNaleznoscInRow(String price, int ilosc, int rowId) {
        BigDecimal cena = new BigDecimal(price);
        String expected = countKoszt(cena, ilosc).toString();
        assEqTextInRow(expected, R.id.tv_charge, rowId);
    }

    private BigDecimal countKoszt(BigDecimal cena, int ilosc) {
        return cena.multiply(new BigDecimal(ilosc)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal countSum(int ilosc) {
        BigDecimal sum = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        return sum.add(countKoszt(new BigDecimal(pgePrices.getZaEnergieCzynna()), ilosc))
                .add(countKoszt(new BigDecimal(pgePrices.getSkladnikJakosciowy()), ilosc))
                .add(countKoszt(new BigDecimal(pgePrices.getOplataSieciowa()), ilosc))
                .add(new BigDecimal(pgePrices.getOplataPrzejsciowa()))
                .add(new BigDecimal(pgePrices.getOplataStalaZaPrzesyl()))
                .add(new BigDecimal(pgePrices.getOplataAbonamentowa()));
    }

    private void assEqText(String expected, int tvId) {
        TextView sumTV = (TextView) sut.findViewById(tvId);
        assertEquals(expected, sumTV.getText().toString());
    }

    private void assEqTextInRow(String expected, int tvId, int rowId) {
        TextView wartoscNettoTV = findTextViewInTableRow(rowId, tvId);
        assertEquals(expected, wartoscNettoTV.getText().toString());
    }

    private TextView findTextViewInTableRow(int rowId, int tvId) {
        TableRow zaEnergieTR = (TableRow) sut.findViewById(rowId);
        return (TextView) zaEnergieTR.findViewById(tvId);
    }

    private void putReadings(Intent intent, int from, int to) {
        intent.putExtra(MainActivity.READING_FROM, from);
        intent.putExtra(MainActivity.READING_TO, to);
        putDates(intent, DATE_FROM, DATE_TO);
    }

    private void putReadings(Intent intent, int dayFrom, int dayTo, int nightFrom, int nightTo) {
        intent.putExtra(MainActivity.READING_DAY_FROM, dayFrom);
        intent.putExtra(MainActivity.READING_DAY_TO, dayTo);
        intent.putExtra(MainActivity.READING_NIGHT_FROM, nightFrom);
        intent.putExtra(MainActivity.READING_NIGHT_TO, nightTo);
        putDates(intent, DATE_FROM, DATE_TO);
    }

    private void putDates(Intent intent, String dateFrom, String dateTo) {
        intent.putExtra(MainActivity.DATE_FROM, dateFrom);
        intent.putExtra(MainActivity.DATE_TO, dateTo);
    }

    private void setDefaultPrices() {
        pgePrices.removeOplataAbonamentowa();
        pgePrices.removeOplataPrzejsciowa();
        pgePrices.removeOplataSieciowa();
        pgePrices.removeOplataSieciowaDzien();
        pgePrices.removeOplataSieciowaNoc();
        pgePrices.removeOplataStalaZaPrzesyl();
        pgePrices.removeSkladnikJakosciowy();
        pgePrices.removeZaEnergieCzynna();
        pgePrices.removeZaEnergieCzynnaDzien();
        pgePrices.removeZaEnergieCzynnaNoc();
    }

    private void setDummyIntent() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.READING_FROM, 1);
        intent.putExtra(MainActivity.READING_TO, 2);
        intent.putExtra(MainActivity.DATE_FROM, "01/01/2014");
        intent.putExtra(MainActivity.DATE_TO, "21/12/2015");
        setActivityIntent(intent);
    }

}
