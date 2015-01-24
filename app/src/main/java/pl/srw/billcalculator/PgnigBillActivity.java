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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.srw.billcalculator.pojo.IPgnigPrices;
import pl.srw.billcalculator.pojo.PgnigPrices;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;

/**
 * Created by Kamil Seweryn
 */
public class PgnigBillActivity extends Activity {

    public static final int PRICE_SCALE = 5;
    public static final BigDecimal VAT = new BigDecimal("0.23");
    public static final String PRICES = "PGNIG_PRICES";
    
    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;
    private IPgnigPrices prices;

    private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal grossCharge;
    private BigDecimal vatAmount;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pgnig_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        setInput(intent);
        setPrices(intent);

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

    private void setInput(Intent intent) {
        dateFrom = intent.getStringExtra(MainActivity.DATE_FROM);
        dateTo = intent.getStringExtra(MainActivity.DATE_TO);
        readingFrom = intent.getIntExtra(MainActivity.READING_FROM, 0);
        readingTo = intent.getIntExtra(MainActivity.READING_TO, 0);
    }

    private void setPrices(Intent intent) {
        if (intent.hasExtra(PRICES))
            prices = (IPgnigPrices) intent.getSerializableExtra(PRICES);
        else
            prices = new PgnigPrices(PreferenceManager.getDefaultSharedPreferences(this));
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
        setTV(readingsTable, R.id.tv_conversion_factor, getString(R.string.wsp_konwersji, prices.getWspolczynnikKonwersji()));
        setTV(readingsTable, R.id.tv_total_consumption_kWh, getString(R.string.zuzycie_razem_kWh, getConsumptionKWh(consumption)));
    }

    private int getConsumptionKWh(int consumption) {
        return new BigDecimal(consumption).multiply(new BigDecimal(prices.getWspolczynnikKonwersji()))
                .setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private int getConsumption() {
        return readingTo - readingFrom;
    }

    private void setChargeDetailsTable() {
        TableLayout chargeTable = (TableLayout) findViewById(R.id.t_charge_details);
        int consumption = getConsumptionKWh(getConsumption());
        int months = Dates.countMonth(dateFrom, dateTo);

        setRow(chargeTable, R.id.row_abonamentowa, R.string.abonamentowa, months, R.string.mc, 
                new BigDecimal(prices.getOplataAbonamentowa()), "");
        setRow(chargeTable, R.id.row_paliwo_gazowe, R.string.paliwo_gazowe, consumption, R.string.kWh, 
                new BigDecimal(prices.getPaliwoGazowe()), "ZW");
        setRow(chargeTable, R.id.row_dystrybucyjna_stala, R.string.dystrybucyjna_stala, months, R.string.mc, 
                new BigDecimal(prices.getDystrybucyjnaStala()), "");
        setRow(chargeTable, R.id.row_dystrybucyjna_zmienna, R.string.dystrybucyjna_zmienna, consumption, R.string.kWh, 
                new BigDecimal(prices.getDystrybucyjnaZmienna()), "");

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
