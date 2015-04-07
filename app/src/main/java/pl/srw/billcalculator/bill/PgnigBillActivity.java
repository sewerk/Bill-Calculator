package pl.srw.billcalculator.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TableLayout;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.calculation.PgnigCalculatedBill;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.pojo.IPgnigPrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;

/**
 * Created by Kamil Seweryn
 */
public class PgnigBillActivity extends BackableActivity {

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

        Intent intent = getIntent();
        setInput(intent);
        setPrices(intent);
        this.bill = new PgnigCalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);

        setDate();
        setReadingsTable();
        setChargeDetailsTable();
        setSummaryTable();
        setChargeTV();
	}

    private void setInput(Intent intent) {
        dateFrom = intent.getStringExtra(IntentCreator.DATE_FROM);
        dateTo = intent.getStringExtra(IntentCreator.DATE_TO);
        readingFrom = intent.getIntExtra(IntentCreator.READING_FROM, 0);
        readingTo = intent.getIntExtra(IntentCreator.READING_TO, 0);
    }

    private void setPrices(Intent intent) {
        if (intent.hasExtra(IntentCreator.PRICES))
            prices = (IPgnigPrices) intent.getSerializableExtra(IntentCreator.PRICES);
        else
            prices = new PgnigPrices();
    }

    private void setDate() {
        Views.setTV(this, R.id.tv_invoice_date, getString(R.string.rozliczenie_dnia, Dates.format(LocalDate.now())));
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
}
