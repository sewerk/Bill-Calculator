package pl.srw.billcalculator.bill.calculation;

import org.junit.Test;

import java.math.BigDecimal;

import pl.srw.billcalculator.db.TauronPrices;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kseweryn on 02.06.15.
 */
public class TauronG12CalculatedBillTest {

    @Test
    public void expectsProperCalculation() {
        // prepare
        final TauronPrices prices = new TauronPrices(1L, null, null, "4.04", "5.05", "6.06", "7.07", "8.08", "9.09", "11.11");

        // calculate
        final TauronG12CalculatedBill sut = new TauronG12CalculatedBill(11, 21, 25, 45, "01/01/2015", "30/10/2015", prices);

        // verify
        assertThat(sut.getDayConsumption(), is(10));
        assertThat(sut.getNightConsumption(), is(20));
        assertThat(sut.getTotalConsumption(), is(30));
        assertThat(sut.getMonthCount(), is(10));

        assertThat(sut.getEnergiaElektrycznaDayNetCharge(), is(new BigDecimal("70.70")));
        assertThat(sut.getOplataDystrybucyjnaZmiennaDayNetCharge(), is(new BigDecimal("90.90")));
        assertThat(sut.getEnergiaElektrycznaNightNetCharge(), is(new BigDecimal("161.60")));
        assertThat(sut.getOplataDystrybucyjnaZmiennaNightNetCharge(), is(new BigDecimal("222.20")));
        assertThat(sut.getOplataPrzejsciowaNetCharge(), is(new BigDecimal("50.50")));
        assertThat(sut.getOplataDystrybucyjnaStalaNetCharge(), is(new BigDecimal("40.40")));
        assertThat(sut.getOplataAbonamentowaNetCharge(), is(new BigDecimal("60.60")));

        assertThat(sut.getEnergiaElektrycznaDayVatCharge(), is(new BigDecimal("16.2610")));
        assertThat(sut.getOplataDystrybucyjnaZmiennaDayVatCharge(), is(new BigDecimal("20.9070")));
        assertThat(sut.getEnergiaElektrycznaNightVatCharge(), is(new BigDecimal("37.1680")));
        assertThat(sut.getOplataDystrybucyjnaZmiennaNightVatCharge(), is(new BigDecimal("51.1060")));
        assertThat(sut.getOplataPrzejsciowaVatCharge(), is(new BigDecimal("11.6150")));
        assertThat(sut.getOplataDystrybucyjnaStalaVatCharge(), is(new BigDecimal("9.2920")));
        assertThat(sut.getOplataAbonamentowaVatCharge(), is(new BigDecimal("13.9380")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("696.90")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("160.29")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("857.19")));
        assertThat(sut.getExcise(), is(new BigDecimal("0.60")));

        assertThat(sut.getSellNetCharge(), is(new BigDecimal("232.30")));
        assertThat(sut.getSellVatCharge(), is(new BigDecimal("53.43")));
        assertThat(sut.getSellGrossCharge(), is(new BigDecimal("285.73")));
        assertThat(sut.getDistributeNetCharge(), is(new BigDecimal("464.60")));
        assertThat(sut.getDistributeVatCharge(), is(new BigDecimal("106.86")));
        assertThat(sut.getDistributeGrossCharge(), is(new BigDecimal("571.46")));
    }
}