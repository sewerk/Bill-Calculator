package pl.srw.billcalculator.bill.calculation;

import org.junit.Test;

import java.math.BigDecimal;

import pl.srw.billcalculator.db.PgePrices;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Kamil Seweryn.
 */
public class PgeG11CalculatedBillTest {

    @Test
    public void expectProperCalculations() {
        // prepare
        final PgePrices prices = new PgePrices(1L, "1.11", "2.02", "3.03", "4.04", "5.05", "6.06", null, null, null, null, "0.00");

        // calculate
        final PgeG11CalculatedBill sut = new PgeG11CalculatedBill(20, 30, "01/03/2015", "30/04/2015", prices);

        // verify
        assertThat(sut.getConsumption(), is(10));
        assertThat(sut.getTotalConsumption(), is(10));
        assertThat(sut.getMonthCount(), is(2));

        assertThat(sut.getZaEnergieCzynnaNetCharge(), is(new BigDecimal("11.10")));
        assertThat(sut.getSkladnikJakosciowyNetCharge(), is(new BigDecimal("20.20")));
        assertThat(sut.getOplataSieciowaNetCharge(), is(new BigDecimal("30.30")));
        assertThat(sut.getOplataPrzejsciowaNetCharge(), is(new BigDecimal("8.08")));
        assertThat(sut.getOplataDystrybucyjnaStalaNetCharge(), is(new BigDecimal("10.10")));
        assertThat(sut.getOplataAbonamentowaNetCharge(), is(new BigDecimal("12.12")));

        assertThat(sut.getZaEnergieCzynnaVatCharge(), is(new BigDecimal("2.5530")));
        assertThat(sut.getSkladnikJakosciowyVatCharge(), is(new BigDecimal("4.6460")));
        assertThat(sut.getOplataSieciowaVatCharge(), is(new BigDecimal("6.9690")));
        assertThat(sut.getOplataPrzejsciowaVatCharge(), is(new BigDecimal("1.8584")));
        assertThat(sut.getOplataDystrybucyjnaStalaVatCharge(), is(new BigDecimal("2.3230")));
        assertThat(sut.getOplataAbonamentowaVatCharge(), is(new BigDecimal("2.7876")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("91.90")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("21.14")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("113.04")));
        assertThat(sut.getExcise(), is(new BigDecimal("0.20")));
    }

    @Test
    public void whenAfterJuly16IncludeOplataOze() throws Exception {
        // GIVEN
        final PgePrices prices = new PgePrices(1L, "1.11", "2.02", "3.03", "4.04", "5.05", "6.06", null, null, null, null, "2.51");

        // WHEN
        final PgeG11CalculatedBill sut = new PgeG11CalculatedBill(20, 30, "01/07/2016", "30/08/2016", prices);

        // THEN
        assertThat(sut.getConsumptionFromJuly16(), is(10));
        assertThat(sut.getOplataOzeNetCharge(), is(new BigDecimal("0.0251")));
        assertThat(sut.getOplataOzeVatCharge(), is(new BigDecimal("0.005773")));
        assertThat(sut.getNetChargeSum(), is(new BigDecimal("91.93")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("21.14")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("113.07")));
        assertThat(sut.getExcise(), is(new BigDecimal("0.20")));
    }
}
