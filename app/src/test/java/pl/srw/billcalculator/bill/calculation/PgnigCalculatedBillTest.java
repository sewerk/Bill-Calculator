package pl.srw.billcalculator.bill.calculation;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import pl.srw.billcalculator.db.PgnigPrices;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigCalculatedBillTest {

    @Test
    public void shouldProperlyCalculateBill() {
        // prepare
        final PgnigPrices prices = new PgnigPrices(1L, "1.01", "2.02", "3.03", "4.04", "5.05");

        // calculate
        final PgnigCalculatedBill sut = new PgnigCalculatedBill(100, 200, "31/07/2015", "24/09/2015", prices);

        // verify
        assertThat(sut.getConsumptionM3(), is(100));
        assertThat(sut.getConsumptionKWh(), is(BigInteger.valueOf(505)));
        assertThat(sut.getMonthCount(), is(2));
        assertThat(sut.getMonthCountExact(), is(new BigDecimal("1.8000")));

        assertThat(sut.getOplataAbonamentowaNetCharge(), is(new BigDecimal("2.02")));
        assertThat(sut.getPaliwoGazoweNetCharge(), is(new BigDecimal("1020.10")));
        assertThat(sut.getDystrybucyjnaStalaNetCharge(), is(new BigDecimal("5.454000")));
        assertThat(sut.getDystrybucyjnaZmiennaNetCharge(), is(new BigDecimal("2040.20")));

        assertThat(sut.getOplataAbonamentowaVatCharge(), is(new BigDecimal("0.4646")));
        assertThat(sut.getPaliwoGazoweVatCharge(), is(new BigDecimal("234.6230")));
        assertThat(sut.getDystrybucyjnaStalaVatCharge(), is(new BigDecimal("1.25442000")));
        assertThat(sut.getDystrybucyjnaZmiennaVatCharge(), is(new BigDecimal("469.2460")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("3067.77")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("705.58")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("3773.35")));
    }
}