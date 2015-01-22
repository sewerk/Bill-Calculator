package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;

/**
 * Created by Kamil Seweryn
 */
public class PgnigBillActivity extends Activity {

    public static final int PRICE_SCALE = 5;
    public static final BigDecimal VAT = new BigDecimal("0.23");
    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;

    private BigDecimal wspKonwersji;
    private BigDecimal oplataAbonamentowa;
    private BigDecimal paliwoGazowe;
    private BigDecimal dystrybucyjnaStala;
    private BigDecimal dystrybucyjnaZmienna;

    private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal grossCharge;
    private BigDecimal vatAmount;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pgnig_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        dateFrom = intent.getStringExtra(MainActivity.DATE_FROM);
        dateTo = intent.getStringExtra(MainActivity.DATE_TO);
        readingFrom = intent.getIntExtra(MainActivity.READING_FROM, 0);
        readingTo = intent.getIntExtra(MainActivity.READING_TO, 0);

        setPrices();

        setReadingsTable();
        setChargeDetailsTable();
        setSummaryTable();
        setChargeTV();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPrices() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String oplataAbonamentowaString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_abonamentowa), getString(R.string.price_abonamentowa));
        oplataAbonamentowa = new BigDecimal(oplataAbonamentowaString);

        String oplataPaliwoGazoweString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_paliwo_gazowe), getString(R.string.price_paliwo_gazowe));
        paliwoGazowe = new BigDecimal(oplataPaliwoGazoweString);

        String dystrybucyjnaStalaString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_dystrybucyjna_stala), getString(R.string.price_dystrybucyjna_stala));
        dystrybucyjnaStala = new BigDecimal(dystrybucyjnaStalaString);

        String dystrybucyjnaZmiennaString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_dystrybucyjna_zmienna), getString(R.string.price_dystrybucyjna_zmienna));
        dystrybucyjnaZmienna = new BigDecimal(dystrybucyjnaZmiennaString);

        String wspKonwersjiString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_wsp_konwersji), getString(R.string.price_wsp_konwersji));
        wspKonwersji = new BigDecimal(wspKonwersjiString);
    }

    private void setReadingsTable() {
        TableLayout readingsTable = (TableLayout) findViewById(R.id.t_readings);

        setTV(readingsTable, R.id.tv_prev_reading_date, dateFrom);
        setTV(readingsTable, R.id.tv_previous_reading, getString(R.string.odczyt_na_dzien, readingFrom));
        setTV(readingsTable, R.id.tv_curr_reading_date, dateTo);
        setTV(readingsTable, R.id.tv_current_reading, getString(R.string.odczyt_na_dzien, readingTo));
        int consumption = getConsumption();
        setTV(readingsTable, R.id.tv_consumption, getString(R.string.zuzycie, consumption));

        setTV(readingsTable, R.id.tv_total_consumption, getString(R.string.zuzycie_razem, consumption));
        setTV(readingsTable, R.id.tv_conversion_factor, getString(R.string.wsp_konwersji, wspKonwersji));
        int consumptionKWh = getConsumptionKWh(consumption);
        setTV(readingsTable, R.id.tv_total_consumption_kWh, getString(R.string.zuzycie_razem_kWh, consumptionKWh));
    }

    private int getConsumptionKWh(int consumption) {
        return new BigDecimal(consumption).multiply(wspKonwersji).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private int getConsumption() {
        return readingTo - readingFrom;
    }

    private void setChargeDetailsTable() {
        TableLayout chargeTable = (TableLayout) findViewById(R.id.t_charge_details);
        int consumption = getConsumptionKWh(getConsumption());
        int months = Dates.countMonth(dateFrom, dateTo);

        setRow(chargeTable, R.id.row_abonamentowa, R.string.abonamentowa, months, R.string.mc, oplataAbonamentowa, "");
        setRow(chargeTable, R.id.row_paliwo_gazowe, R.string.paliwo_gazowe, consumption, R.string.kWh, paliwoGazowe, "ZW");
        setRow(chargeTable, R.id.row_dystrybucyjna_stala, R.string.dystrybucyjna_stala, months, R.string.mc, dystrybucyjnaStala, "");
        setRow(chargeTable, R.id.row_dystrybucyjna_zmienna, R.string.dystrybucyjna_zmienna, consumption, R.string.kWh, dystrybucyjnaZmienna, "");

        setChargeSummary(chargeTable);
    }

    private void setRow(TableLayout chargeTable, int rowId, int descId, int count, int jmId, BigDecimal netPrice, String exciseAmount) {
        View row = chargeTable.findViewById(rowId);
        setTV(row, R.id.tv_charge_desc, getString(descId));
        setTV(row, R.id.tv_date_from, dateFrom);
        setTV(row, R.id.tv_date_to, dateTo);
        setTV(row, R.id.tv_Jm, getString(jmId));
        if (jmId == R.string.kWh) {
            setTV(row, R.id.tv_count, "" + count);
        } else {
            setTV(row, R.id.tv_count, Display.withScale(new BigDecimal(count), 3));
        }
        setTV(row, R.id.tv_net_price, Display.withScale(netPrice, PRICE_SCALE));
        setTV(row, R.id.tv_excise, exciseAmount);
        setTV(row, R.id.tv_net_charge, getCharge(count, netPrice));

    }

    private String getCharge(int count, BigDecimal price) {
        BigDecimal charge = price.multiply(new BigDecimal(count));
        netChargeSum = netChargeSum.add(charge);
        return Display.toPay(charge);
    }

    private void setChargeSummary(View chargeTable) {
        View summaryRow = chargeTable.findViewById(R.id.row_sum);

        setTV(summaryRow, R.id.tv_net_charge, Display.toPay(netChargeSum));
        vatAmount = netChargeSum.multiply(VAT);
        setTV(summaryRow, R.id.tv_vat_amount, Display.toPay(vatAmount));
        grossCharge = netChargeSum.add(vatAmount);
        setTV(summaryRow, R.id.tv_gross_charge, Display.toPay(grossCharge));
    }

    private void setSummaryTable() {
        TableLayout summaryTable = (TableLayout) findViewById(R.id.t_summary);

        setTV(summaryTable, R.id.tv_net_charge, Display.toPay(netChargeSum));
        setTV(summaryTable, R.id.tv_vat_amount, Display.toPay(vatAmount));
        setTV(summaryTable, R.id.tv_gross_charge, Display.toPay(grossCharge));
    }

    private void setChargeTV() {
        setTV(R.id.tv_invoice_value, getString(R.string.wartosc_faktury, Display.toPay(grossCharge)));
    }

    private void setTV(View parent, int tvId, String text) {
        TextView tv = (TextView) parent.findViewById(tvId);
        setTV(tv, text);
    }

    private void setTV(int tvId, String text) {
        TextView tv = (TextView) findViewById(tvId);
        setTV(tv, text);
    }

    private void setTV(TextView tv, String text) {
        tv.setText(text);
    }

}
