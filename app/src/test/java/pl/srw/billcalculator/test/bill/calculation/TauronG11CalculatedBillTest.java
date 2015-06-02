package pl.srw.billcalculator.test.bill.calculation;

import org.junit.Test;

import java.math.BigDecimal;

import pl.srw.billcalculator.bill.calculation.TauronG11CalculatedBill;
import pl.srw.billcalculator.db.TauronPrices;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kseweryn on 02.06.15.
 */
public class TauronG11CalculatedBillTest {

    @Test
    public void expectProperCalculations() {
        // prepare
        final TauronPrices prices = new TauronPrices(1L, "1.11", "2.02", "3.03", "4.04", "5.05", null, null, null, null);

        // calculate
        final TauronG11CalculatedBill sut = new TauronG11CalculatedBill(20, 30, "01/03/2015", "30/04/2015", prices);

        // verify
        assertThat(sut.getConsumption(), is(10));
        assertThat(sut.getTotalConsumption(), is(10));
        assertThat(sut.getMonthCount(), is(2));

        assertThat(sut.getEnergiaElektrycznaNetCharge(), is(new BigDecimal("11.10")));
        assertThat(sut.getOplataDystrybucyjnaZmiennaNetCharge(), is(new BigDecimal("20.20")));
        assertThat(sut.getOplataDystrybucyjnaStalaNetCharge(), is(new BigDecimal("6.06")));
        assertThat(sut.getOplataPrzejsciowaNetCharge(), is(new BigDecimal("8.08")));
        assertThat(sut.getOplataAbonamentowaNetCharge(), is(new BigDecimal("10.10")));

        assertThat(sut.getEnergiaElektrycznaVatCharge(), is(new BigDecimal("2.5530")));
        assertThat(sut.getOplataDystrybucyjnaZmiennaVatCharge(), is(new BigDecimal("4.6460")));
        assertThat(sut.getOplataDystrybucyjnaStalaVatCharge(), is(new BigDecimal("1.3938")));
        assertThat(sut.getOplataPrzejsciowaVatCharge(), is(new BigDecimal("1.8584")));
        assertThat(sut.getOplataAbonamentowaVatCharge(), is(new BigDecimal("2.3230")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("55.54")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("12.77")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("68.31")));
        assertThat(sut.getExcise(), is(new BigDecimal("0.20")));
    }
}
