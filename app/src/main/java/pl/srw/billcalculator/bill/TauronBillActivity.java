package pl.srw.billcalculator.bill;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TableRow;

import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.Optional;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.calculation.TauronCalculatedBill;
import pl.srw.billcalculator.bill.calculation.TauronG11CalculatedBill;
import pl.srw.billcalculator.bill.calculation.TauronG12CalculatedBill;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.pojo.ITauronPrices;
import pl.srw.billcalculator.settings.prices.TauronPrices;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;
import pl.srw.billcalculator.util.Views;

/**
 * Created by kseweryn on 23.04.15.
 */
public class TauronBillActivity extends EnergyBillActivity {

    public static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final int PRICE_SCALE = 5;

    @Optional @InjectExtra(IntentCreator.PRICES) ITauronPrices prices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tauron_bill);

        if (prices == null)
            prices = new TauronPrices();

        bill = isTwoUnitTariff() ?
                new TauronG12CalculatedBill(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, prices)
                : new TauronG11CalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);

        setDates();
        setTariff();
        setTable();
        setExcise();
    }

    private void setDates() {
        Views.setTV(this, R.id.tv_invoice_date, getString(R.string.data_wystawienia, Dates.format(LocalDate.now(), DATE_PATTERN)));
        Views.setTV(this, R.id.tv_title, getString(R.string.data_sprzedazy, Dates.format(Dates.parse(dateTo), "MM.yyyy")));
        Views.setTV(this, R.id.tv_for_period, getString(R.string.okres_rozliczeniowy, format(dateFrom), format(dateTo)));
    }

    private void setTariff() {
        Views.setTV(this, R.id.tv_tariff, getString(R.string.grupa_taryfowa, getTariff()));
    }

    private void setExcise() {
        Views.setTV(this, R.id.tv_excise, getString(R.string.kwota_akcyzy, Display.toPay(bill.getExcise())));
    }

    private void setTable() {
        if (isTwoUnitTariff())
            setG12Rows();
        else
            setG11Rows();
        setRow(R.id.row_oplata_dyst_stala, R.string.tauron_oplata_dyst_stala, "", "", "", bill.getMonthCount(), "", new BigDecimal(prices.getOplataDystrybucyjnaStala()), bill.getOplataDystrybucyjnaStalaNetCharge());
        setRow(R.id.row_oplata_przejsciowa, R.string.tauron_oplata_przejsciowa, "", "", "", bill.getMonthCount(), "", new BigDecimal(prices.getOplataPrzejsciowa()), bill.getOplataPrzejsciowaNetCharge());
        setRow(R.id.row_oplata_abonamentowa, R.string.tauron_oplata_abonamentowa, "", "", "", bill.getMonthCount(), "", new BigDecimal(prices.getOplataAbonamentowa()), bill.getOplataAbonamentowaNetCharge());

        setSumarry();
    }

    private void setG12Rows() {
        final TauronG12CalculatedBill bill = (TauronG12CalculatedBill) this.bill;
        setRow(R.id.row_za_energie_czynna, R.string.tauron_energia_elektryczna_G12dzien, "00000000", "" + readingDayFrom, "" + readingDayTo, 1, "" + bill.getDayConsumption(), new BigDecimal(prices.getEnergiaElektrycznaCzynnaDzien()), bill.getEnergiaElektrycznaDayNetCharge());
        setRow(R.id.row_za_energie_czynna2, R.string.tauron_energia_elektryczna_G12noc, "00000000", "" + readingNightFrom, "" + readingNightTo, 1, "" + bill.getNightConsumption(), new BigDecimal(prices.getEnergiaElektrycznaCzynnaNoc()), bill.getEnergiaElektrycznaNightNetCharge());
        findViewById(R.id.row_za_energie_czynna2).setBackgroundResource(R.drawable.undeline);
        setRow(R.id.row_oplata_dyst_zm, R.string.tauron_oplata_dyst_zmienna_G12dzien, "00000000", "" + readingDayFrom, "" + readingDayTo, 1, "" + bill.getDayConsumption(), new BigDecimal(prices.getOplataDystrybucyjnaZmiennaDzien()), bill.getOplataDystrybucyjnaZmiennaDayNetCharge());
        setRow(R.id.row_oplata_dyst_zm2, R.string.tauron_oplata_dyst_zmienna_G12noc, "00000000", "" + readingNightFrom, "" + readingNightTo, 1, "" + bill.getNightConsumption(), new BigDecimal(prices.getOplataDystrybucyjnaZmiennaNoc()), bill.getOplataDystrybucyjnaZmiennaNightNetCharge());
    }

    private void setG11Rows() {
        final TauronG11CalculatedBill bill = (TauronG11CalculatedBill) this.bill;
        final String prevReading = Integer.toString(readingFrom);
        final String currReading = Integer.toString(readingTo);
        final String consumption = Integer.toString(bill.getConsumption());
        setRow(R.id.row_za_energie_czynna, R.string.tauron_energia_elektryczna, "00000000", prevReading, currReading, 1, consumption, new BigDecimal(prices.getEnergiaElektrycznaCzynna()), bill.getEnergiaElektrycznaNetCharge());
        findViewById(R.id.row_za_energie_czynna).setBackgroundResource(R.drawable.undeline);
        findViewById(R.id.row_za_energie_czynna2).setVisibility(View.GONE);
        setRow(R.id.row_oplata_dyst_zm, R.string.tauron_oplata_dyst_zmienna, "00000000", prevReading, currReading, 1, consumption, new BigDecimal(prices.getOplataDystrybucyjnaZmienna()), bill.getOplataDystrybucyjnaZmiennaNetCharge());
        findViewById(R.id.row_oplata_dyst_zm2).setVisibility(View.GONE);
    }

    private void setRow(@IdRes int rowId, @StringRes int descId, String meterNo, String prevReading, String currReading, int count, String consumption, BigDecimal price, BigDecimal amount) {
        final TableRow row = (TableRow) findViewById(rowId);
        Views.setTVInRow(row, R.id.tv_description, descId);
        Views.setTVInRow(row, R.id.tv_meter_no, meterNo);
        Views.setTVInRow(row, R.id.tv_prev_date, format(dateFrom));
        Views.setTVInRow(row, R.id.tv_prev_reading, prevReading);
        Views.setTVInRow(row, R.id.tv_curr_date, format(dateTo));
        Views.setTVInRow(row, R.id.tv_curr_reading, currReading);
        Views.setTVInRow(row, R.id.tv_count, Integer.toString(count));
        Views.setTVInRow(row, R.id.tv_consumption, consumption);
        Views.setTVInRow(row, R.id.tv_price, Display.withScale(price, PRICE_SCALE));
        Views.setTVInRow(row, R.id.tv_amount, Display.toPay(amount));
    }

    private void setSumarry() {
        Views.setTV(this, R.id.tv_total_consumption, "" + bill.getTotalConsumption());
        Views.setTV(this, R.id.tv_component_net_amount, Display.toPay(bill.getNetChargeSum()));
        setSummaryDetails();
        Views.setTV(this, R.id.tv_total_amount, Display.toPay(bill.getGrossChargeSum()));
    }

    private void setSummaryDetails() {
        final TauronCalculatedBill bill = (TauronCalculatedBill) this.bill;

        Views.setTV(this, R.id.tv_sell_net, Display.toPay(bill.getSellNetCharge()));
        Views.setTV(this, R.id.tv_sell_vat, Display.toPay(bill.getSellVatCharge()));
        Views.setTV(this, R.id.tv_sell_gross, Display.toPay(bill.getSellGrossCharge()));
        Views.setTV(this, R.id.tv_distribute_net, Display.toPay(bill.getDistributeNetCharge()));
        Views.setTV(this, R.id.tv_distribute_vat, Display.toPay(bill.getDistributeVatCharge()));
        Views.setTV(this, R.id.tv_distribute_gross, Display.toPay(bill.getDistributeGrossCharge()));
        Views.setTV(this, R.id.tv_total_net_amount, Display.toPay(bill.getNetChargeSum()));
        Views.setTV(this, R.id.tv_total_vat_amount, Display.toPay(bill.getVatChargeSum()));
        Views.setTV(this, R.id.tv_total_gross_amount, Display.toPay(bill.getGrossChargeSum()));
    }

    private String getTariff() {
        final String tariff;
        if (isTwoUnitTariff()) tariff = "G12";
        else tariff = "G11";
        return tariff;
    }

    private String format(String date) {
        return Dates.format(Dates.parse(date), DATE_PATTERN);
    }

}
