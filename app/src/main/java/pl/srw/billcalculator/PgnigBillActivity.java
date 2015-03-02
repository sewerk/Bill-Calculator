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

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.calculation.PgnigCalculatedBill;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.pojo.IPgnigPrices;
import pl.srw.billcalculator.preference.PgnigPrices;
import pl.srw.billcalculator.task.BillStorer;
import pl.srw.billcalculator.task.PgnigBillStorer;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;

/**
 * Created by Kamil Seweryn
 */
public class PgnigBillActivity extends Activity {

    public static final int PRICE_SCALE = 5;

    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;

    private IPgnigPrices prices;
    private PgnigCalculatedBill bill;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pgnig_bill);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        setInput(intent);
        setPrices(intent);
        this.bill = new PgnigCalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);

        setReadingsTable();
        setChargeDetailsTable();
        setSummaryTable();
        setChargeTV();
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

    private void setInput(Intent intent) {
        dateFrom = intent.getStringExtra(BillActivityIntentFactory.DATE_FROM);
        dateTo = intent.getStringExtra(BillActivityIntentFactory.DATE_TO);
        readingFrom = intent.getIntExtra(BillActivityIntentFactory.READING_FROM, 0);
        readingTo = intent.getIntExtra(BillActivityIntentFactory.READING_TO, 0);
    }

    private void setPrices(Intent intent) {
        if (intent.hasExtra(BillActivityIntentFactory.PRICES))
            prices = (IPgnigPrices) intent.getSerializableExtra(BillActivityIntentFactory.PRICES);
        else
            prices = new PgnigPrices();
    }

    private void setReadingsTable() {
        TableLayout readingsTable = (TableLayout) findViewById(R.id.t_readings);

        Views.setTV(readingsTable, R.id.tv_prev_reading_date, dateFrom);
        Views.setTV(readingsTable, R.id.tv_previous_reading, getString(R.string.odczyt_na_dzien, readingFrom));
        Views.setTV(readingsTable, R.id.tv_curr_reading_date, dateTo);
        Views.setTV(readingsTable, R.id.tv_current_reading, getString(R.string.odczyt_na_dzien, readingTo));
        Views.setTV(readingsTable, R.id.tv_consumption, getString(R.string.zuzycie, bill.getConsumptionM3()));

        Views.setTV(readingsTable, R.id.tv_total_consumption, getString(R.string.zuzycie_razem, bill.getConsumptionM3()));
        Views.setTV(readingsTable, R.id.tv_conversion_factor, getString(R.string.wsp_konwersji, prices.getWspolczynnikKonwersji()));
        Views.setTV(readingsTable, R.id.tv_total_consumption_kWh, getString(R.string.zuzycie_razem_kWh, bill.getConsumptionKWh()));
    }

    private void setChargeDetailsTable() {
        TableLayout chargeTable = (TableLayout) findViewById(R.id.t_charge_details);

        setRow(chargeTable, R.id.row_abonamentowa, R.string.abonamentowa, bill.getMonthCount(), R.string.mc,
                new BigDecimal(prices.getOplataAbonamentowa()), bill.getOplataAbonamentowaNetCharge(), "");
        setRow(chargeTable, R.id.row_paliwo_gazowe, R.string.paliwo_gazowe, bill.getConsumptionKWh(), R.string.kWh,
                new BigDecimal(prices.getPaliwoGazowe()), bill.getPaliwoGazoweNetCharge(), "ZW");
        setRow(chargeTable, R.id.row_dystrybucyjna_stala, R.string.dystrybucyjna_stala, bill.getMonthCount(), R.string.mc,
                new BigDecimal(prices.getDystrybucyjnaStala()), bill.getDystrybucyjnaStalaNetCharge(), "");
        setRow(chargeTable, R.id.row_dystrybucyjna_zmienna, R.string.dystrybucyjna_zmienna, bill.getConsumptionKWh(), R.string.kWh,
                new BigDecimal(prices.getDystrybucyjnaZmienna()), bill.getDystrybucyjnaZmiennaNetCharge(), "");

        setChargeSummary(chargeTable);
    }

    private void setRow(TableLayout chargeTable, @IdRes int rowId, @StringRes int descId, int count, @StringRes int jmId,
                        BigDecimal netPrice, BigDecimal netCharge, String exciseAmount) {
        View row = chargeTable.findViewById(rowId);
        Views.setTV(row, R.id.tv_charge_desc, getString(descId));
        Views.setTV(row, R.id.tv_date_from, dateFrom);
        Views.setTV(row, R.id.tv_date_to, dateTo);
        Views.setTV(row, R.id.tv_Jm, getString(jmId));
        if (jmId == R.string.kWh) {
            Views.setTV(row, R.id.tv_count, "" + count);
        } else {
            Views.setTV(row, R.id.tv_count, Display.withScale(new BigDecimal(count), 3));
        }
        Views.setTV(row, R.id.tv_net_price, Display.withScale(netPrice, PRICE_SCALE));
        Views.setTV(row, R.id.tv_excise, exciseAmount);
        Views.setTV(row, R.id.tv_net_charge, Display.toPay(netCharge));

    }

    private void setChargeSummary(View chargeTable) {
        View summaryRow = chargeTable.findViewById(R.id.row_sum);

        Views.setTV(summaryRow, R.id.tv_net_charge, Display.toPay(bill.getNetChargeSum()));
        Views.setTV(summaryRow, R.id.tv_vat_amount, Display.toPay(bill.getVatChargeSum()));
        Views.setTV(summaryRow, R.id.tv_gross_charge, Display.toPay(bill.getGrossChargeSum()));
    }

    private void setSummaryTable() {
        TableLayout summaryTable = (TableLayout) findViewById(R.id.t_summary);

        Views.setTV(summaryTable, R.id.tv_net_charge, Display.toPay(bill.getNetChargeSum()));
        Views.setTV(summaryTable, R.id.tv_vat_amount, Display.toPay(bill.getVatChargeSum()));
        Views.setTV(summaryTable, R.id.tv_gross_charge, Display.toPay(bill.getGrossChargeSum()));
    }

    private void setChargeTV() {
        Views.setTV(this, R.id.tv_invoice_value, getString(R.string.wartosc_faktury, Display.toPay(bill.getGrossChargeSum())));
    }

    private void saveBill() {
        BillStorer task = new PgnigBillStorer(readingFrom, readingTo);
        task.putDates(dateFrom, dateTo);
        task.putAmount(bill.getGrossChargeSum().doubleValue());

        new Thread(task).start();
    }
}
