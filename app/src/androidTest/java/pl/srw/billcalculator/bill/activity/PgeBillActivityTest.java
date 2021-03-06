package pl.srw.billcalculator.bill.activity;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TableRow;
import android.widget.TextView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.math.BigDecimal;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.settings.prices.PgePrices;

public class PgeBillActivityTest extends ActivityInstrumentationTestCase2<PgeBillActivity> {

    private static final LocalDate DATE_FROM = LocalDate.of(2014, Month.DECEMBER, 1);
    private static final LocalDate DATE_TO = LocalDate.of(2014, Month.DECEMBER, 31);
    private static final int FROM = 12;
    private static final int TO = 25;

    private PgeBillActivity sut;
    private PgePrices pgePrices;

    public PgeBillActivityTest() {
        super(PgeBillActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context targetContext = getInstrumentation().getTargetContext();
        pgePrices = new PgePrices(PreferenceManager.getDefaultSharedPreferences(targetContext));
        setDummyIntent();
        setDefaultPrices();
    }

    public void testTaryfaLabelG11DependOnIntentExtra() {
        Intent intent = new Intent();
        putReadings(intent, FROM, TO);
        setActivityIntent(intent);

        sut = getActivity();
        String expected = sut.getString(R.string.tariff_G11_on_bill);
        assEqText(expected, R.id.tv_tariff);
    }

    public void testTaryfaLabelG12DependOnIntentExtra() {
        Intent intent = new Intent();
        putReadings(intent, FROM, TO, FROM, TO);
        setActivityIntent(intent);

        sut = getActivity();
        String expected = sut.getString(R.string.tariff_G12_on_bill);
        assEqText(expected, R.id.tv_tariff);
    }

    public void testZaOkresLabelDependsOnIntentExtra() {
        final LocalDate dateFrom = LocalDate.of(2014, Month.DECEMBER, 3);
        final LocalDate dateTo = LocalDate.of(2014, Month.DECEMBER, 29);

        Intent intent = new Intent();
        putReadings(intent, FROM, TO);
        putDates(intent, dateFrom, dateTo);
        setActivityIntent(intent);

        sut = getActivity();
        String expected = "Za okres od 03/12/2014 do 29/12/2014";
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

        String expected = sut.getString(R.string.akcyza, diff, "" + (diff * 0.02));
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

        String expected = sut.getString(R.string.akcyza, diff, "" + (diff * 0.02));
        assEqText(expected, R.id.tv_excise);
    }

    public void testCalculationForG11Readings() {
        final int ilosc = TO - FROM;
        Intent intent = new Intent();
        putReadings(intent, FROM, TO);
        setActivityIntent(intent);

        sut = getActivity();
        checkChargeInRow(pgePrices.getZaEnergieCzynna(), ilosc, R.id.row_za_energie_czynna);
        checkChargeInRow(pgePrices.getSkladnikJakosciowy(), ilosc, R.id.row_skladnik_jakosciowy);
        checkChargeInRow(pgePrices.getOplataSieciowa(), ilosc, R.id.row_oplata_sieciowa);
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
        assEqText(expected, R.id.pge_sum_net_charge);

        BigDecimal vatCost = countVatSum(ilosc);
        assEqText(vatCost.toString(), R.id.pge_sum_vat_amount);

        BigDecimal sumAll = sumNetto.add(vatCost);
        assEqText(sumAll.toString(), R.id.pge_sum_gross_charge);

        String doZaplatyExpected = sut.getString(R.string.to_pay, sumAll);
        assEqText(doZaplatyExpected, R.id.tv_to_pay);
    }

    public void testCalculationForG12Readings() {
        Intent intent = new Intent();
        putReadings(intent, 0, 10, 0, 100);
        setActivityIntent(intent);

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

        assEqText("1 112.01", R.id.tv_total_net_charge);
        assEqText("1 112.01", R.id.pge_sum_net_charge);
        assEqText("255.76", R.id.pge_sum_vat_amount);
        assEqText("1 367.77", R.id.pge_sum_gross_charge);
    }

    // ================================================================ private methods
    private void checkReadingsValuesInRow(int expectedPoprzednie, int expectedBiezace, int expectedIlosc, int rowId) {
        assEqTextInRow("" + expectedBiezace, R.id.tv_current_reading, rowId);
        assEqTextInRow("" + expectedPoprzednie, R.id.tv_previous_reading, rowId);
        assEqTextInRow("" + expectedIlosc, R.id.tv_count, rowId);
    }

    private void checkChargeInRow(String price, int ilosc, int rowId) {
        BigDecimal bdPrice = new BigDecimal(price);
        String expected = countCost(bdPrice, ilosc).toString();
        assEqTextInRow(expected, R.id.tv_charge, rowId);
    }

    private BigDecimal countCost(BigDecimal cena, int ilosc) {
        return cena.multiply(new BigDecimal(ilosc))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal countSum(int ilosc) {
        BigDecimal sum = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        return sum.add(countCost(new BigDecimal(pgePrices.getZaEnergieCzynna()), ilosc))
                .add(countCost(new BigDecimal(pgePrices.getSkladnikJakosciowy()), ilosc))
                .add(countCost(new BigDecimal(pgePrices.getOplataSieciowa()), ilosc))
                .add(new BigDecimal(pgePrices.getOplataPrzejsciowa()))
                .add(new BigDecimal(pgePrices.getOplataStalaZaPrzesyl()))
                .add(new BigDecimal(pgePrices.getOplataAbonamentowa()));
    }

    private BigDecimal countVatSum(int ilosc) {
        return BigDecimal.ZERO
                .add(countVat(new BigDecimal(pgePrices.getZaEnergieCzynna()), ilosc))
                .add(countVat(new BigDecimal(pgePrices.getSkladnikJakosciowy()), ilosc))
                .add(countVat(new BigDecimal(pgePrices.getOplataSieciowa()), ilosc))
                .add(countVat(new BigDecimal(pgePrices.getOplataPrzejsciowa()), 1))
                .add(countVat(new BigDecimal(pgePrices.getOplataStalaZaPrzesyl()),1))
                .add(countVat(new BigDecimal(pgePrices.getOplataAbonamentowa()), 1))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal countVat(BigDecimal cena, int ilosc) {
        return cena.multiply(new BigDecimal(ilosc))
                .multiply(new BigDecimal("0.23"));
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
        intent.putExtra(IntentCreator.READING_FROM, from);
        intent.putExtra(IntentCreator.READING_TO, to);
        putDates(intent, DATE_FROM, DATE_TO);
    }

    private void putReadings(Intent intent, int dayFrom, int dayTo, int nightFrom, int nightTo) {
        intent.putExtra(IntentCreator.READING_DAY_FROM, dayFrom);
        intent.putExtra(IntentCreator.READING_DAY_TO, dayTo);
        intent.putExtra(IntentCreator.READING_NIGHT_FROM, nightFrom);
        intent.putExtra(IntentCreator.READING_NIGHT_TO, nightTo);
        putDates(intent, DATE_FROM, DATE_TO);
    }

    private void putDates(Intent intent, LocalDate dateFrom, LocalDate dateTo) {
        intent.putExtra(IntentCreator.DATE_FROM, dateFrom);
        intent.putExtra(IntentCreator.DATE_TO, dateTo);
    }

    private void setDefaultPrices() {
        pgePrices.setDefault();
    }

    private void setDummyIntent() {
        Intent intent = new Intent();
        intent.putExtra(IntentCreator.READING_FROM, 1);
        intent.putExtra(IntentCreator.READING_TO, 2);
        intent.putExtra(IntentCreator.DATE_FROM, LocalDate.of(2014, Month.JANUARY, 1));
        intent.putExtra(IntentCreator.DATE_TO, LocalDate.of(2015, Month.DECEMBER, 21));
        setActivityIntent(intent);
    }

}