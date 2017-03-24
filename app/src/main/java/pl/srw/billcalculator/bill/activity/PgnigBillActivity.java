package pl.srw.billcalculator.bill.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TableLayout;

import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.Optional;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import javax.inject.Inject;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.bill.calculation.PgnigCalculatedBill;
import pl.srw.billcalculator.bill.di.PgnigBillComponent;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.pojo.IPgnigPrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.billcalculator.wrapper.Dependencies;

public class PgnigBillActivity extends BillActivity<PgnigBillComponent> {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final int PRICE_SCALE = 5;

    @InjectExtra(IntentCreator.DATE_FROM) String dateFrom;
    @InjectExtra(IntentCreator.DATE_TO) String dateTo;
    @InjectExtra(IntentCreator.READING_FROM) int readingFrom;
    @InjectExtra(IntentCreator.READING_TO) int readingTo;

    @Optional @InjectExtra(IntentCreator.PRICES) IPgnigPrices prices;
    @Inject PgnigPrices prefsPrices;
    @Inject SavedBillsRegistry savedBillsRegistry;

    private PgnigCalculatedBill bill;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dependencies.inject(this);
        Analytics.logContent(ContentType.PGNIG_BILL, "PGNIG new", String.valueOf(prices == null));

        if (prices == null) {
            prices = prefsPrices;
        }
        this.bill = new PgnigCalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);

        setDate();
        setReadingsTable();
        setChargeDetailsTable();
        setSummaryTable();
        setChargeTV();
    }

    @Override
    public PgnigBillComponent prepareComponent() {
        return BillCalculator.get(this).getApplicationComponent().getPgnigBillComponent();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.pgnig_bill;
    }

    @Override
    protected String getBillIdentifier() {
        return savedBillsRegistry.getIdentifier(Provider.PGNIG, readingFrom, readingTo, dateFrom, dateTo, prices);
    }

    private void setDate() {
        Views.setTV(this, R.id.tv_invoice_date, getString(R.string.rozliczenie_dnia, Dates.format(LocalDate.now(), DATE_PATTERN)));
    }

    private void setReadingsTable() {
        TableLayout readingsTable = (TableLayout) findViewById(R.id.t_readings);

        Views.setTV(readingsTable, R.id.tv_prev_reading_date, Dates.changeSeparator(dateFrom, "."));
        Views.setTV(readingsTable, R.id.tv_previous_reading, getString(R.string.odczyt_na_dzien, readingFrom));
        Views.setTV(readingsTable, R.id.tv_curr_reading_date, Dates.changeSeparator(dateTo, "."));
        Views.setTV(readingsTable, R.id.tv_current_reading, getString(R.string.odczyt_na_dzien, readingTo));
        Views.setTV(readingsTable, R.id.tv_consumption, getString(R.string.zuzycie, bill.getConsumptionM3()));

        Views.setTV(readingsTable, R.id.tv_total_consumption, getString(R.string.zuzycie_razem, bill.getConsumptionM3()));
        Views.setTV(readingsTable, R.id.tv_conversion_factor, getString(R.string.wsp_konwersji, prices.getWspolczynnikKonwersji()));
        Views.setTV(readingsTable, R.id.tv_total_consumption_kWh, getString(R.string.zuzycie_razem_kWh, bill.getConsumptionKWh()));
    }

    private void setChargeDetailsTable() {
        TableLayout chargeTable = (TableLayout) findViewById(R.id.t_charge_details);

        setRow(chargeTable, R.id.row_abonamentowa, R.string.abonamentowa, ""+bill.getMonthCount()+".0000", R.string.mc,
                new BigDecimal(prices.getOplataAbonamentowa()), bill.getOplataAbonamentowaNetCharge(), "");
        setRow(chargeTable, R.id.row_paliwo_gazowe, R.string.paliwo_gazowe, bill.getConsumptionKWh().toString(), R.string.kWh,
                new BigDecimal(prices.getPaliwoGazowe()), bill.getPaliwoGazoweNetCharge(), "ZW");
        setRow(chargeTable, R.id.row_dystrybucyjna_stala, R.string.dystrybucyjna_stala, ""+bill.getMonthCountExact(), R.string.mc,
                new BigDecimal(prices.getDystrybucyjnaStala()), bill.getDystrybucyjnaStalaNetCharge(), "");
        setRow(chargeTable, R.id.row_dystrybucyjna_zmienna, R.string.dystrybucyjna_zmienna, bill.getConsumptionKWh().toString(), R.string.kWh,
                new BigDecimal(prices.getDystrybucyjnaZmienna()), bill.getDystrybucyjnaZmiennaNetCharge(), "");

        setChargeSummary(chargeTable);
    }

    private void setRow(TableLayout chargeTable, @IdRes int rowId, @StringRes int descId, String count, @StringRes int jmId,
                        BigDecimal netPrice, BigDecimal netCharge, String exciseAmount) {
        View row = chargeTable.findViewById(rowId);
        Views.setTV(row, R.id.tv_charge_desc, getString(descId));
        Views.setTV(row, R.id.tv_date_from, Dates.changeSeparator(dateFrom, "."));
        Views.setTV(row, R.id.tv_date_to, Dates.changeSeparator(dateTo, "."));
        Views.setTV(row, R.id.tv_Jm, getString(jmId));
        Views.setTV(row, R.id.tv_count, count);
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
