package pl.srw.billcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TableLayout;

import java.math.BigDecimal;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.calculation.PgeCalculatedBill;
import pl.srw.billcalculator.calculation.PgeG11CalculatedBill;
import pl.srw.billcalculator.calculation.PgeG12CalculatedBill;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.pojo.IPgePrices;
import pl.srw.billcalculator.preference.PgePrices;
import pl.srw.billcalculator.task.BillStorer;
import pl.srw.billcalculator.task.PgeBillStorer;
import pl.srw.billcalculator.task.PgeG12BillStorer;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;

/**
 * Created by Kamil Seweryn
 */
public class PgeBillActivity extends BackableActivity {

    public static final int PRICE_SCALE = 4;

    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;
    private int readingDayFrom;
    private int readingDayTo;
    private int readingNightFrom;
    private int readingNightTo;

    private IPgePrices prices;
    private PgeCalculatedBill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pge_bill);

        final Intent intent = getIntent();
        readExtra(intent);
        setPrices(intent);

        bill = isTwoUnitTariff() ?
                new PgeG12CalculatedBill(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, prices)
                : new PgeG11CalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);

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

    private void readExtra(Intent intent) {
        dateFrom = intent.getStringExtra(IntentCreator.DATE_FROM);
        dateTo = intent.getStringExtra(IntentCreator.DATE_TO);

        readingFrom = intent.getIntExtra(IntentCreator.READING_FROM, -1);
        readingTo = intent.getIntExtra(IntentCreator.READING_TO, -1);

        readingDayFrom = intent.getIntExtra(IntentCreator.READING_DAY_FROM, -1);
        readingDayTo = intent.getIntExtra(IntentCreator.READING_DAY_TO, -1);
        readingNightFrom = intent.getIntExtra(IntentCreator.READING_NIGHT_FROM, -1);
        readingNightTo = intent.getIntExtra(IntentCreator.READING_NIGHT_TO, -1);
    }

    private void setPrices(Intent intent) {
        if (intent.hasExtra(IntentCreator.PRICES))
            prices = (IPgePrices) intent.getSerializableExtra(IntentCreator.PRICES);
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

        setRow(chargeDetailsTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, bill.getMonthCount(), R.string.m_c,
                new BigDecimal(prices.getOplataPrzejsciowa()), bill.getOplataPrzejsciowaNetCharge());
        setRow(chargeDetailsTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, bill.getMonthCount(), R.string.m_c,
                new BigDecimal(prices.getOplataStalaZaPrzesyl()), bill.getOplataStalaZaPrzesylNetCharge());
        setRow(chargeDetailsTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, bill.getMonthCount(), R.string.m_c,
                new BigDecimal(prices.getOplataAbonamentowa()), bill.getOplataAbonamentowaNetCharge());

        setChargeSummary(chargeDetailsTable);
    }

    private boolean isTwoUnitTariff() {
        return readingDayTo > 0;
    }

    private void setG11Rows(TableLayout chargeDetailsTable, int consumption) {
        PgeG11CalculatedBill bill = (PgeG11CalculatedBill) this.bill;
        setRow(chargeDetailsTable, R.id.row_za_energie_czynna, R.string.strefa_calodobowa, R.string.za_energie_czynna, consumption, R.string.kWh,
                new BigDecimal(prices.getZaEnergieCzynna()), bill.getZaEnergieCzynnaNetCharge());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy, R.string.strefa_calodobowa, R.string.skladnik_jakosciowy, consumption, R.string.kWh,
                new BigDecimal(prices.getSkladnikJakosciowy()), bill.getSkladnikJakosciowyNetCharge());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa, R.string.strefa_calodobowa, R.string.oplata_sieciowa, consumption, R.string.kWh,
                new BigDecimal(prices.getOplataSieciowa()), bill.getOplataSieciowaNetCharge());
    }

    private void setG12Rows(TableLayout chargeDetailsTable, int dayConsumption, int nightConsumption) {
        PgeG12CalculatedBill bill = (PgeG12CalculatedBill) this.bill;
        setRow(chargeDetailsTable, R.id.row_za_energie_czynna, R.string.strefa_dzienna, R.string.za_energie_czynna, dayConsumption, R.string.kWh,
                new BigDecimal(prices.getZaEnergieCzynnaDzien()), bill.getZaEnergieCzynnaDayNetCharge());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy, R.string.strefa_dzienna, R.string.skladnik_jakosciowy, dayConsumption, R.string.kWh,
                new BigDecimal(prices.getSkladnikJakosciowy()), bill.getSkladnikJakosciowyDayNetCharge());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa, R.string.strefa_dzienna, R.string.oplata_sieciowa, dayConsumption, R.string.kWh,
                new BigDecimal(prices.getOplataSieciowaDzien()), bill.getOplataSieciowaDayNetCharge());

        setRow(chargeDetailsTable, R.id.row_za_energie_czynna2, R.string.strefa_nocna, R.string.za_energie_czynna, nightConsumption, R.string.kWh,
                new BigDecimal(prices.getZaEnergieCzynnaNoc()), bill.getZaEnergieCzynnaNightNetCharge());
        setRow(chargeDetailsTable, R.id.row_skladnik_jakosciowy2, R.string.strefa_nocna, R.string.skladnik_jakosciowy, nightConsumption, R.string.kWh,
                new BigDecimal(prices.getSkladnikJakosciowy()), bill.getSkladnikJakosciowyNightNetCharge());
        setRow(chargeDetailsTable, R.id.row_oplata_sieciowa2, R.string.strefa_nocna, R.string.oplata_sieciowa, nightConsumption, R.string.kWh,
                new BigDecimal(prices.getOplataSieciowaNoc()), bill.getOplataSieciowaNightNetCharge());
    }

    private void setRow(View componentsTable, @IdRes int rowId, @StringRes int zoneId, @StringRes int descriptionId, int count, @StringRes int jmId,
                        BigDecimal netPrice, BigDecimal netCharge) {
        View row = componentsTable.findViewById(rowId);
        row.setVisibility(View.VISIBLE);

        Views.setTVInRow(row, R.id.tv_zone, zoneId);
        Views.setTVInRow(row, R.id.tv_description, descriptionId);
        Views.setTVInRow(row, R.id.tv_Jm, jmId);
        if (jmId == R.string.kWh) {
            setReadingsInRow(row, zoneId);
            Views.setTVInRow(row, R.id.tv_count, Integer.toString(count));
        } else {
            Views.setTVInRow(row, R.id.tv_month_count, Integer.toString(count) + ".00");
        }
        Views.setTVInRow(row, R.id.tv_charge, Display.toPay(netCharge));
        Views.setTVInRow(row, R.id.tv_price, Display.withScale(netPrice, PRICE_SCALE));
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

    private int countConsumption() {
        return ((PgeG11CalculatedBill)bill).getConsumption();
    }

    private int countDayConsumption() {
        return ((PgeG12CalculatedBill) bill).getDayConsumption();
    }

    private int countNightConsumption() {
        return ((PgeG12CalculatedBill) bill).getNightConsumption();
    }

    private void setChargeSummary(View table) {
        View summary = table.findViewById(R.id.row_sum);
        Views.setTVInRow(summary, R.id.tv_total_net_charge, Display.toPay(bill.getNetChargeSum()));
    }

    private void setExcise() {
        Views.setTV(this, R.id.tv_excise, getString(R.string.akcyza, bill.getTotalConsumption(), Display.toPay(bill.getExcise())));
    }

    private void setSummaryTable() {
        TableLayout summaryTable = (TableLayout) findViewById(R.id.t_summary);
        Views.setTVInRow(summaryTable, R.id.tv_net_charge, Display.toPay(bill.getNetChargeSum()));
        Views.setTVInRow(summaryTable, R.id.tv_vat_amount, Display.toPay(bill.getVatChargeSum()));
        Views.setTVInRow(summaryTable, R.id.tv_gross_charge, Display.toPay(bill.getGrossChargeSum()));
    }

    private void setToPayTV() {
        Views.setTV(this, R.id.tv_to_pay, getString(R.string.to_pay, Display.toPay(bill.getGrossChargeSum())));
    }

    private void saveBill() {
        BillStorer task;
        if (isTwoUnitTariff()) {
            task = new PgeG12BillStorer(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo);
        } else {
            task = new PgeBillStorer(readingFrom, readingTo);
        }
        task.putDates(dateFrom, dateTo);
        task.putAmount(bill.getGrossChargeSum().doubleValue());

        new Thread(task).start();
    }


}
