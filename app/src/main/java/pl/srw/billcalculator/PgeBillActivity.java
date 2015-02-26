package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.pojo.IPgePrices;
import pl.srw.billcalculator.preference.PgePrices;
import pl.srw.billcalculator.task.BillStorer;
import pl.srw.billcalculator.task.PgeBillStorer;
import pl.srw.billcalculator.task.PgeG12BillStorer;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;

/**
 * Created by Kamil Seweryn
 */
public class PgeBillActivity extends Activity {

    public static final int PRICE_SCALE = 4;
    private static final BigDecimal EXCISE = new BigDecimal("0.02");
    private static final BigDecimal VAT = new BigDecimal("0.23");
    public static final String PRICES = "PGE_PRICES";//TODO move to IntentFactory
    private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal grossCharge;

    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;
    private int readingDayFrom;
    private int readingDayTo;
    private int readingNightFrom;
    private int readingNightTo;
    private IPgePrices prices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pge_bill);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        readExtra(intent);
        setPrices(intent);

        setForPeriodTV();
        setChargeDetailsTable();
        setExcise();
        setSummaryTable();
        setToPayTV();
    }

    @DebugLog
    @Override
    protected void onResume() {
        super.onResume();
        saveBill();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readExtra(Intent intent) {
        dateFrom = intent.getStringExtra(BillActivityIntentFactory.DATE_FROM);
        dateTo = intent.getStringExtra(BillActivityIntentFactory.DATE_TO);

        readingFrom = intent.getIntExtra(BillActivityIntentFactory.READING_FROM, -1);
        readingTo = intent.getIntExtra(BillActivityIntentFactory.READING_TO, -1);

        readingDayFrom = intent.getIntExtra(BillActivityIntentFactory.READING_DAY_FROM, -1);
        readingDayTo = intent.getIntExtra(BillActivityIntentFactory.READING_DAY_TO, -1);
        readingNightFrom = intent.getIntExtra(BillActivityIntentFactory.READING_NIGHT_FROM, -1);
        readingNightTo = intent.getIntExtra(BillActivityIntentFactory.READING_NIGHT_TO, -1);
    }

    private void setPrices(Intent intent) {
        if (intent.hasExtra(PRICES))
            prices = (IPgePrices) intent.getSerializableExtra(PRICES);
        else 
            prices = new PgePrices();
    }

    private void setForPeriodTV() {
        Views.setTV(this, R.id.tv_for_period, getString(R.string.for_period, dateFrom, dateTo));
    }

    private void setChargeDetailsTable() {
        TableLayout chargeDetailsTable = (TableLayout) findViewById(R.id.t_charge_details);

        if(isTwoUnitTariff()) {
            Views.setTV(this, R.id.tv_tariff, getString(R.string.pge_tariff_G12_on_bill));
            setG12Rows(chargeDetailsTable, countDayConsumption(), countNightConsumption());
        } else {
            Views.setTV(this, R.id.tv_tariff, getString(R.string.pge_tariff_G11_on_bill));
            setG11Rows(chargeDetailsTable, countConsumption());
        }

        final int months = Dates.countMonth(dateFrom, dateTo);
        setRow(chargeDetailsTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, months, R.string.m_c, prices.getOplataPrzejsciowa());
        setRow(chargeDetailsTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, months, R.string.m_c, prices.getOplataStalaZaPrzesyl());
        setRow(chargeDetailsTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, months, R.string.m_c, prices.getOplataAbonamentowa());

        setChargeSummary(chargeDetailsTable);
    }

    private boolean isTwoUnitTariff() {
        return readingDayTo > 0;
    }

    private void setG11Rows(TableLayout chargeDetailsTable, int consumption) {
        setRow(chargeDetailsTable, R.id.row_za_energie_czynna, R.string.strefa_calodobowa, R.string.za_energie_czynna, consumption, R.string.kWh, prices.getZaEnergieCzynna());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy, R.string.strefa_calodobowa, R.string.skladnik_jakosciowy, consumption, R.string.kWh, prices.getSkladnikJakosciowy());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa, R.string.strefa_calodobowa, R.string.oplata_sieciowa, consumption, R.string.kWh, prices.getOplataSieciowa());
    }

    private void setG12Rows(TableLayout chargeDetailsTable, int dayConsumption, int nightConsumption) {
        setRow(chargeDetailsTable, R.id.row_za_energie_czynna, R.string.strefa_dzienna, R.string.za_energie_czynna, dayConsumption, R.string.kWh, prices.getZaEnergieCzynnaDzien());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy, R.string.strefa_dzienna, R.string.skladnik_jakosciowy, dayConsumption, R.string.kWh, prices.getSkladnikJakosciowy());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa, R.string.strefa_dzienna, R.string.oplata_sieciowa, dayConsumption, R.string.kWh, prices.getOplataSieciowaDzien());

        setRow(chargeDetailsTable, R.id.row_za_energie_czynna2, R.string.strefa_nocna, R.string.za_energie_czynna, nightConsumption, R.string.kWh, prices.getZaEnergieCzynnaNoc());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy2, R.string.strefa_nocna, R.string.skladnik_jakosciowy, nightConsumption, R.string.kWh, prices.getSkladnikJakosciowy());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa2, R.string.strefa_nocna, R.string.oplata_sieciowa, nightConsumption, R.string.kWh, prices.getOplataSieciowaNoc());
    }

    private void setRow(View componentsTable, @IdRes int rowId, @StringRes int zoneId, @StringRes int descriptionId, int count, @StringRes int jmId, String priceSt) {
        View row = componentsTable.findViewById(rowId);
        row.setVisibility(View.VISIBLE);

        Views.setTVInRow(row, R.id.tv_zone, zoneId);
        Views.setTVInRow(row, R.id.tv_description, descriptionId);
        Views.setTVInRow(row, R.id.tv_Jm, jmId);
        if (jmId == R.string.kWh) {
            setReadingsInRow(row, zoneId);
            Views.setTVInRow(row, R.id.tv_count, Integer.toString(count));
        } else {
            Views.setTVInRow(row, R.id.tv_month_count, Display.withScale(new BigDecimal(count), 2));
        }
        BigDecimal price = new BigDecimal(priceSt);
        Views.setTVInRow(row, R.id.tv_charge, getNetCharge(price, count));
        Views.setTVInRow(row, R.id.tv_price, Display.withScale(price, PRICE_SCALE));
    }

    private void setReadingsInRow(View row, @StringRes int zoneId) {
        if (zoneId == R.string.strefa_dzienna) {
            Views.setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingDayTo));
            Views.setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingDayFrom));
        } else if (zoneId == R.string.strefa_nocna) {
            Views.setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingNightTo));
            Views.setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingNightFrom));
        } else {
            Views.setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingTo));
            Views.setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingFrom));
        }
    }

    private String getNetCharge(BigDecimal price, int count) {
        BigDecimal charge = price.multiply(new BigDecimal(count));
        netChargeSum = netChargeSum.add(charge);
        return Display.toPay(charge);
    }

    private int countConsumption() {
        return readingTo - readingFrom;
    }

    private int countDayConsumption() {
        return readingDayTo - readingDayFrom;
    }

    private int countNightConsumption() {
        return readingNightTo - readingNightFrom;
    }

    private void setChargeSummary(View table) {
        View summary = table.findViewById(R.id.row_sum);
        Views.setTVInRow(summary, R.id.tv_total_net_charge, Display.toPay(netChargeSum));
    }

    private void setExcise() {
        int consumption = isTwoUnitTariff()
                ? countDayConsumption() + countNightConsumption()
                : countConsumption();
        BigDecimal excise = EXCISE.multiply(new BigDecimal(consumption));
        Views.setTV(this, R.id.tv_excise, getString(R.string.akcyza, consumption, Display.toPay(excise)));
    }

    private void setSummaryTable() {
        TableLayout summaryTable = (TableLayout) findViewById(R.id.t_summary);
        Views.setTVInRow(summaryTable, R.id.tv_net_charge, Display.toPay(netChargeSum));
        BigDecimal vatAmount = netChargeSum.multiply(VAT).setScale(2, RoundingMode.HALF_UP);
        Views.setTVInRow(summaryTable, R.id.tv_vat_amount, Display.toPay(vatAmount));
        grossCharge = netChargeSum.add(vatAmount);
        Views.setTVInRow(summaryTable, R.id.tv_gross_charge, Display.toPay(grossCharge));
    }

    private void setToPayTV() {
        Views.setTV(this, R.id.tv_to_pay, getString(R.string.to_pay, Display.toPay(grossCharge)));
    }

    private void saveBill() {
        BillStorer task;
        if (isTwoUnitTariff()) {
            task = new PgeG12BillStorer(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo);
        } else {
            task = new PgeBillStorer(readingFrom, readingTo);
        }
        task.putDates(dateFrom, dateTo);
        task.putAmount(grossCharge.doubleValue());

        new Thread(task).start();
    }


}
