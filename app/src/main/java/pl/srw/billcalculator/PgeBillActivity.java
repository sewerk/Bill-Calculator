package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.data.PgePrices;
import pl.srw.billcalculator.task.BillStorer;
import pl.srw.billcalculator.task.PgeBillStorer;
import pl.srw.billcalculator.task.PgeG12BillStorer;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;

/**
 * Created by Kamil Seweryn
 */
public class PgeBillActivity extends Activity {

    public static final int PRICE_SCALE = 4;
    private static final BigDecimal EXCISE = new BigDecimal("0.02");
    private static final BigDecimal VAT = new BigDecimal("0.23");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pge_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        readExtra(getIntent());
        setPrices();

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
        dateFrom = intent.getStringExtra(MainActivity.DATE_FROM);
        dateTo = intent.getStringExtra(MainActivity.DATE_TO);

        readingFrom = intent.getIntExtra(MainActivity.READING_FROM, -1);
        readingTo = intent.getIntExtra(MainActivity.READING_TO, -1);

        readingDayFrom = intent.getIntExtra(MainActivity.READING_DAY_FROM, -1);
        readingDayTo = intent.getIntExtra(MainActivity.READING_DAY_TO, -1);
        readingNightFrom = intent.getIntExtra(MainActivity.READING_NIGHT_FROM, -1);
        readingNightTo = intent.getIntExtra(MainActivity.READING_NIGHT_TO, -1);
    }

    private void setPrices() {
        PgePrices.INSTANCE.read(this);

    }

    private void setForPeriodTV() {
        setTV(R.id.tv_for_period, getString(R.string.for_period, dateFrom, dateTo));
    }

    private void setChargeDetailsTable() {
        TableLayout chargeDetailsTable = (TableLayout) findViewById(R.id.t_charge_details);

        if(isTwoUnitTariff()) {
            setTV(R.id.tv_tariff, getString(R.string.pge_tariff_G12_on_bill));
            setG12Rows(chargeDetailsTable, countDayConsumption(), countNightConsumption());
        } else {
            setTV(R.id.tv_tariff, getString(R.string.pge_tariff_G11_on_bill));
            setG11Rows(chargeDetailsTable, countConsumption());
        }

        PgePrices prices = PgePrices.INSTANCE;
        final int months = Dates.countMonth(dateFrom, dateTo);
        setRow(chargeDetailsTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, months, R.string.m_c, prices.getCenaOplataPrzejsciowa());
        setRow(chargeDetailsTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, months, R.string.m_c, prices.getCenaOplStalaZaPrzesyl());
        setRow(chargeDetailsTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, months, R.string.m_c, prices.getCenaOplataAbonamentowa());

        setChargeSummary(chargeDetailsTable);
    }

    private boolean isTwoUnitTariff() {
        return readingDayTo > 0;
    }

    private void setG11Rows(TableLayout chargeDetailsTable, int consumption) {
        PgePrices prices = PgePrices.INSTANCE;

        setRow(chargeDetailsTable, R.id.row_za_energie_czynna, R.string.strefa_calodobowa, R.string.za_energie_czynna, consumption, R.string.kWh, prices.getCenaZaEnergieCzynna());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy, R.string.strefa_calodobowa, R.string.skladnik_jakosciowy, consumption, R.string.kWh, prices.getCenaSkladnikJakosciowy());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa, R.string.strefa_calodobowa, R.string.oplata_sieciowa, consumption, R.string.kWh, prices.getCenaOplataSieciowa());
    }

    private void setG12Rows(TableLayout chargeDetailsTable, int dayConsumption, int nightConsumption) {
        PgePrices prices = PgePrices.INSTANCE;

        setRow(chargeDetailsTable, R.id.row_za_energie_czynna, R.string.strefa_dzienna, R.string.za_energie_czynna, dayConsumption, R.string.kWh, prices.getCenaZaEnergieCzynnaDzien());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy, R.string.strefa_dzienna, R.string.skladnik_jakosciowy, dayConsumption, R.string.kWh, prices.getCenaSkladnikJakosciowy());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa, R.string.strefa_dzienna, R.string.oplata_sieciowa, dayConsumption, R.string.kWh, prices.getCenaOplataSieciowaDzien());

        setRow(chargeDetailsTable, R.id.row_za_energie_czynna2, R.string.strefa_nocna, R.string.za_energie_czynna, nightConsumption, R.string.kWh, prices.getCenaZaEnergieCzynnaNoc());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy2, R.string.strefa_nocna, R.string.skladnik_jakosciowy, nightConsumption, R.string.kWh, prices.getCenaSkladnikJakosciowy());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa2, R.string.strefa_nocna, R.string.oplata_sieciowa, nightConsumption, R.string.kWh, prices.getCenaOplataSieciowaNoc());
    }

    private void setRow(View componentsTable, int rowId, int zoneId, int descriptionId, int count, int jmId, BigDecimal price) {
        View row = componentsTable.findViewById(rowId);
        row.setVisibility(View.VISIBLE);

        setTVInRow(row, R.id.tv_zone, zoneId);
        setTVInRow(row, R.id.tv_description, descriptionId);
        setTVInRow(row, R.id.tv_Jm, jmId);
        if (jmId == R.string.kWh) {
            setReadingsInRow(row, zoneId);
            setTVInRow(row, R.id.tv_count, Integer.toString(count));
        } else {
            setTVInRow(row, R.id.tv_month_count, Display.withScale(new BigDecimal(count), 2));
        }
        setTVInRow(row, R.id.tv_charge, getNetCharge(price, count));
        setTVInRow(row, R.id.tv_price, Display.withScale(price, PRICE_SCALE));
    }

    private void setReadingsInRow(View row, int zoneId) {
        if (zoneId == R.string.strefa_dzienna) {
            setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingDayTo));
            setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingDayFrom));
        } else if (zoneId == R.string.strefa_nocna) {
            setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingNightTo));
            setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingNightFrom));
        } else {
            setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingTo));
            setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingFrom));
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

    private void setTVInRow(View row, int tvId, int stringId) {
        TextView tv = (TextView) row.findViewById(tvId);
        setTV(tv, getString(stringId));
    }

    private void setTVInRow(View row, int tvId, String string) {
        TextView tv = (TextView) row.findViewById(tvId);
        setTV(tv, string);
    }

    private void setTV(TextView tv, String string) {
        tv.setText(string);
    }

    private void setTV(int tvId, String string) {
        TextView tv = (TextView) findViewById(tvId);
        tv.setText(string);
    }

    private void setChargeSummary(View table) {
        View summary = table.findViewById(R.id.row_sum);
        setTVInRow(summary, R.id.tv_total_net_charge, Display.toPay(netChargeSum));
    }

    private void setExcise() {
        int consumption = isTwoUnitTariff()
                ? countDayConsumption() + countNightConsumption()
                : countConsumption();
        BigDecimal excise = EXCISE.multiply(new BigDecimal(consumption));
        setTV(R.id.tv_excise, getString(R.string.akcyza, consumption, Display.toPay(excise)));
    }

    private void setSummaryTable() {
        TableLayout summaryTable = (TableLayout) findViewById(R.id.t_summary);
        setTVInRow(summaryTable, R.id.tv_net_charge, Display.toPay(netChargeSum));
        BigDecimal vatAmount = netChargeSum.multiply(VAT).setScale(2, RoundingMode.HALF_UP);
        setTVInRow(summaryTable, R.id.tv_vat_amount, Display.toPay(vatAmount));
        grossCharge = netChargeSum.add(vatAmount);
        setTVInRow(summaryTable, R.id.tv_gross_charge, Display.toPay(grossCharge));
    }

    private void setToPayTV() {
        setTV(R.id.tv_to_pay, getString(R.string.to_pay, Display.toPay(grossCharge)));
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
