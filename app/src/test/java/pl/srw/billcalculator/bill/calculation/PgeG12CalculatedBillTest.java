package pl.srw.billcalculator.bill.calculation;

import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.math.BigDecimal;

import pl.srw.billcalculator.db.PgePrices;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Kamil Seweryn.
 */
public class PgeG12CalculatedBillTest {

    @Test
    public void expectProperCalculations() {
        // prepare
        final PgePrices prices = new PgePrices(1L, null, "2.02", null, "4.04", "5.05", "6.06", "7.07", "8.08", "9.09", "11.11", "0.00");

        // calculate
        final PgeG12CalculatedBill sut = new PgeG12CalculatedBill(20, 30, 35, 55, LocalDate.of(2015, Month.JANUARY, 1), LocalDate.of(2015, Month.OCTOBER, 30), prices);

        // verify
        assertThat(sut.getDayConsumption(), is(10));
        assertThat(sut.getNightConsumption(), is(20));
        assertThat(sut.getTotalConsumption(), is(30));
        assertThat(sut.getMonthCount(), is(10));

        assertThat(sut.getZaEnergieCzynnaDayNetCharge(), is(new BigDecimal("70.70")));
        assertThat(sut.getSkladnikJakosciowyDayNetCharge(), is(new BigDecimal("20.20")));
        assertThat(sut.getOplataSieciowaDayNetCharge(), is(new BigDecimal("90.90")));
        assertThat(sut.getZaEnergieCzynnaNightNetCharge(), is(new BigDecimal("161.60")));
        assertThat(sut.getSkladnikJakosciowyNightNetCharge(), is(new BigDecimal("40.40")));
        assertThat(sut.getOplataSieciowaNightNetCharge(), is(new BigDecimal("222.20")));
        assertThat(sut.getOplataPrzejsciowaNetCharge(), is(new BigDecimal("40.40")));
        assertThat(sut.getOplataDystrybucyjnaStalaNetCharge(), is(new BigDecimal("50.50")));
        assertThat(sut.getOplataAbonamentowaNetCharge(), is(new BigDecimal("60.60")));

        assertThat(sut.getZaEnergieCzynnaDayVatCharge(), is(new BigDecimal("16.2610")));
        assertThat(sut.getSkladnikJakosciowyDayVatCharge(), is(new BigDecimal("4.6460")));
        assertThat(sut.getOplataSieciowaDayVatCharge(), is(new BigDecimal("20.9070")));
        assertThat(sut.getZaEnergieCzynnaNightVatCharge(), is(new BigDecimal("37.1680")));
        assertThat(sut.getSkladnikJakosciowyNightVatCharge(), is(new BigDecimal("9.2920")));
        assertThat(sut.getOplataSieciowaNightVatCharge(), is(new BigDecimal("51.1060")));
        assertThat(sut.getOplataPrzejsciowaVatCharge(), is(new BigDecimal("9.2920")));
        assertThat(sut.getOplataDystrybucyjnaStalaVatCharge(), is(new BigDecimal("11.6150")));
        assertThat(sut.getOplataAbonamentowaVatCharge(), is(new BigDecimal("13.9380")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("757.50")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("174.23")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("931.73")));
        assertThat(sut.getExcise(), is(new BigDecimal("0.60")));
    }

    @Test
    public void whenAfterJuly16IncludeOplataOze() throws Exception {
        // prepare
        final PgePrices prices = new PgePrices(1L, null, "2.02", null, "4.04", "5.05", "6.06", "7.07", "8.08", "9.09", "11.11", "2.51");

        // calculate
        final PgeG12CalculatedBill sut = new PgeG12CalculatedBill(20, 30, 35, 55, LocalDate.of(2016, Month.JULY, 1), LocalDate.of(2017, Month.APRIL, 30), prices);

        // verify
        assertThat(sut.getDayConsumptionFromJuly16(), is(10));
        assertThat(sut.getNightConsumptionFromJuly16(), is(20));

        assertThat(sut.getOplataOzeDayNetCharge(), is(new BigDecimal("0.0251")));
        assertThat(sut.getOplataOzeNightNetCharge(), is(new BigDecimal("0.0502")));
        assertThat(sut.getOplataOzeDayVatCharge(), is(new BigDecimal("0.005773")));
        assertThat(sut.getOplataOzeNightVatCharge(), is(new BigDecimal("0.011546")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("757.58")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("174.24")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("931.82")));
        assertThat(sut.getExcise(), is(new BigDecimal("0.60")));
    }
}