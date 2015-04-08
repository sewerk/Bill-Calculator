package pl.srw.billcalculator.test.bill.calculation;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import pl.srw.billcalculator.bill.calculation.PgnigCalculatedBill;
import pl.srw.billcalculator.db.PgnigPrices;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
public class PgnigCalculatedBillTest {

    @Test
    public void shouldProperlyCalculateBill() {
        // prepare
        final PgnigPrices prices = new PgnigPrices(1L, "1.01", "2.02", "3.03", "4.04", "5.05");

        // calculate
        final PgnigCalculatedBill sut = new PgnigCalculatedBill(100, 200, "01/03/2015", "30/04/2015", prices);

        // verify
        assertThat(sut.getConsumptionM3(), is(100));
        assertThat(sut.getConsumptionKWh(), is(505));
        assertThat(sut.getMonthCount(), is(2));

        assertThat(sut.getOplataAbonamentowaNetCharge(), is(new BigDecimal("2.02")));
        assertThat(sut.getPaliwoGazoweNetCharge(), is(new BigDecimal("1020.10")));
        assertThat(sut.getDystrybucyjnaStalaNetCharge(), is(new BigDecimal("6.06")));
        assertThat(sut.getDystrybucyjnaZmiennaNetCharge(), is(new BigDecimal("2040.20")));

        assertThat(sut.getOplataAbonamentowaVatCharge(), is(new BigDecimal("0.4646")));
        assertThat(sut.getPaliwoGazoweVatCharge(), is(new BigDecimal("234.6230")));
        assertThat(sut.getDystrybucyjnaStalaVatCharge(), is(new BigDecimal("1.3938")));
        assertThat(sut.getDystrybucyjnaZmiennaVatCharge(), is(new BigDecimal("469.2460")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("3068.38")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("705.72")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("3774.10")));
    }
}
