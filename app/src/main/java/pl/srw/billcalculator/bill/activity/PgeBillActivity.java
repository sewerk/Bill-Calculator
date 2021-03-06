package pl.srw.billcalculator.bill.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TableLayout;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.math.BigDecimal;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.calculation.PgeG11CalculatedBill;
import pl.srw.billcalculator.bill.calculation.PgeG12CalculatedBill;
import pl.srw.billcalculator.bill.di.PgeBillComponent;
import pl.srw.billcalculator.di.Dependencies;
import pl.srw.billcalculator.dialog.BillCalculatedBeforeOZEChangeDialogFragment;
import pl.srw.billcalculator.pojo.IPgePrices;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;
import pl.srw.billcalculator.util.analytics.Analytics;
import pl.srw.billcalculator.util.analytics.ContentType;

public class PgeBillActivity extends EnergyBillActivity<IPgePrices, PgePrices, PgeBillComponent> {

    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final int PRICE_SCALE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Analytics.contentView(ContentType.BILL,
                "view bill", "PGE" + (isTwoUnitTariff() ? "G12" : "G11"),
                "view bill from history", !isNewBill());

        if (!isNewBill()
                && savedInstanceState == null
                && "0.00".equals(prices.getOplataOze())
                && dateTo.isAfter(LocalDate.of(2016, Month.JULY, 1))
                && dateTo.isBefore(LocalDate.ofYearDay(2018, 1))) {
            new BillCalculatedBeforeOZEChangeDialogFragment()
                    .show(getFragmentManager(), null);
        }

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
        return Dependencies.getApplicationComponent().getPgeBillComponent();
    }

    @Override
    protected int getLayoutId() {
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
        Views.setTV(this, R.id.tv_title, getString(R.string.rachunek_rozliczeniowy, Dates.format(LocalDate.now(), DATE_PATTERN)));
        Views.setTV(this, R.id.tv_for_period, getString(R.string.for_period, Dates.format(dateFrom, DATE_PATTERN), Dates.format(dateTo, DATE_PATTERN)));
    }

    private void setChargeDetailsTable() {
        TableLayout chargeDetailsTable = findViewById(R.id.t_charge_details);

        if (isTwoUnitTariff()) {
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
        setOplataHandlowaRow(chargeDetailsTable);
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

        if (bill.getConsumptionFromJuly16() > 0) {
            setRow(chargeDetailsTable, R.id.row_oplata_oze, R.string.strefa_calodobowa, R.string.oplata_oze, bill.getConsumptionFromJuly16(), R.string.MWh,
                    new BigDecimal(prices.getOplataOze()), bill.getOplataOzeNetCharge());
        }
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

        if (bill.getDayConsumptionFromJuly16() > 0 || bill.getNightConsumptionFromJuly16() > 0) {
            setRow(chargeDetailsTable, R.id.row_oplata_oze, R.string.strefa_dzienna, R.string.oplata_oze, bill.getDayConsumptionFromJuly16(), R.string.MWh,
                    new BigDecimal(prices.getOplataOze()), bill.getOplataOzeDayNetCharge());
            setRow(chargeDetailsTable, R.id.row_oplata_oze2, R.string.strefa_nocna, R.string.oplata_oze, bill.getNightConsumptionFromJuly16(), R.string.MWh,
                    new BigDecimal(prices.getOplataOze()), bill.getOplataOzeNightNetCharge());
        }
    }

    private void setOplataHandlowaRow(TableLayout chargeDetailsTable) {
        if (isOplataHandlowaEnabled()) {
            setRow(chargeDetailsTable, R.id.row_oplata_handlowa, R.string.strefa_pusta, R.string.pge_bill_oplata_handlowa, bill.getMonthCount(), R.string.m_c,
                    new BigDecimal(prices.getOplataHandlowa()), bill.getOplataHandlowaNetCharge());
        } else {
            chargeDetailsTable.findViewById(R.id.row_oplata_handlowa).setVisibility(View.GONE);
        }
    }

    private void setRow(View componentsTable, @IdRes int rowId, @StringRes int zoneId, @StringRes int descriptionId, int count, @StringRes int jmId,
                        BigDecimal netPrice, BigDecimal netCharge) {
        View row = componentsTable.findViewById(rowId);
        row.setVisibility(View.VISIBLE);

        Views.setTVInRow(row, R.id.tv_zone, zoneId);
        Views.setTVInRow(row, R.id.tv_description, descriptionId);
        Views.setTVInRow(row, R.id.tv_Jm, jmId);
        if (jmId == R.string.kWh) {
            setReadingsInRow(row, zoneId, count);
            Views.setTVInRow(row, R.id.tv_count, Integer.toString(count));
        } else if (jmId == R.string.MWh) {
            setReadingsInRow(row, zoneId, count);
            Views.setTVInRow(row, R.id.tv_count, String.format(Dates.PL_LOCALE, "%.3f", (count * 0.001)));
        } else {
            Views.setTVInRow(row, R.id.tv_month_count, Integer.toString(count) + ".00");
        }
        Views.setTVInRow(row, R.id.tv_charge, Display.toPay(netCharge));
        Views.setTVInRow(row, R.id.tv_price, Display.withScale(netPrice, PRICE_SCALE));
    }

    private void setReadingsInRow(View row, @StringRes int zoneId, int count) {
        if (zoneId == R.string.strefa_dzienna) {
            Views.setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingDayTo));
            Views.setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingDayTo - count));
        } else if (zoneId == R.string.strefa_nocna) {
            Views.setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingNightTo));
            Views.setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingNightTo - count));
        } else {
            Views.setTVInRow(row, R.id.tv_current_reading, Integer.toString(readingTo));
            Views.setTVInRow(row, R.id.tv_previous_reading, Integer.toString(readingTo - count));
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
        TableLayout summaryTable = findViewById(R.id.t_summary);
        Views.setTVInRow(summaryTable, R.id.pge_sum_net_charge, Display.toPay(bill.getNetChargeSum()));
        Views.setTVInRow(summaryTable, R.id.pge_sum_vat_amount, Display.toPay(bill.getVatChargeSum()));
        Views.setTVInRow(summaryTable, R.id.pge_sum_gross_charge, Display.toPay(bill.getGrossChargeSum()));
    }

    private void setToPayTV() {
        Views.setTV(this, R.id.tv_to_pay, getString(R.string.to_pay, Display.toPay(bill.getGrossChargeSum())));
    }

}
