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
import pl.srw.billcalculator.bill.calculation.PgeG11CalculatedBill;
import pl.srw.billcalculator.bill.calculation.PgeG12CalculatedBill;
import pl.srw.billcalculator.bill.di.PgeBillComponent;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.pojo.IPgePrices;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.billcalculator.wrapper.Dependencies;

public class PgeBillActivity extends EnergyBillActivity<PgeBillComponent> {

    private static final int PRICE_SCALE = 4;

    @Optional @InjectExtra(IntentCreator.PRICES) IPgePrices prices;

    @Inject PgePrices prefsPrices;
    @Inject SavedBillsRegistry savedBillsRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dependencies.inject(this);
        Analytics.logContent(ContentType.PGE_BILL,
                "PGE new", String.valueOf(prices == null),
                "PGE tariff", (isTwoUnitTariff() ? "G12" : "G11"));

        if (prices == null)
            prices = prefsPrices;

        bill = isTwoUnitTariff() ?
                new PgeG12CalculatedBill(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, prices)
                : new PgeG11CalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);

        setDates();
        setChargeDetailsTable();
        setExcise();
        setSummaryTable();
        setToPayTV();
    }

    @Override
    public PgeBillComponent prepareComponent() {
        // FIXME: change to application scope component
        return BillCalculator.get(this).getApplicationComponent().getPgeBillComponent();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.pge_bill;
    }

    @Override
    protected String getBillIdentifier() {
        if (isTwoUnitTariff())
            return savedBillsRegistry.getIdentifier(Provider.PGE, readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, prices);
        else
            return savedBillsRegistry.getIdentifier(Provider.PGE, readingFrom, readingTo, dateFrom, dateTo, prices);
    }

    private void setDates() {
        Views.setTV(this, R.id.tv_title, getString(R.string.rachunek_rozliczeniowy, Dates.format(LocalDate.now())));
        Views.setTV(this, R.id.tv_for_period, getString(R.string.for_period, dateFrom, dateTo));
    }

    private void setChargeDetailsTable() {
        TableLayout chargeDetailsTable = (TableLayout) findViewById(R.id.t_charge_details);

        if(isTwoUnitTariff()) {
            Views.setTV(this, R.id.tv_tariff, getString(R.string.tariff_G12_on_bill));
            setG12Rows(chargeDetailsTable, countDayConsumption(), countNightConsumption());
        } else {
            Views.setTV(this, R.id.tv_tariff, getString(R.string.tariff_G11_on_bill));
            setG11Rows(chargeDetailsTable, countConsumption());
        }

        setRow(chargeDetailsTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, bill.getMonthCount(), R.string.m_c,
                new BigDecimal(prices.getOplataPrzejsciowa()), bill.getOplataPrzejsciowaNetCharge());
        setRow(chargeDetailsTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, bill.getMonthCount(), R.string.m_c,
                new BigDecimal(prices.getOplataStalaZaPrzesyl()), bill.getOplataDystrybucyjnaStalaNetCharge());
        setRow(chargeDetailsTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, bill.getMonthCount(), R.string.m_c,
                new BigDecimal(prices.getOplataAbonamentowa()), bill.getOplataAbonamentowaNetCharge());

        setChargeSummary(chargeDetailsTable);
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
        return bill.getTotalConsumption();
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

}
