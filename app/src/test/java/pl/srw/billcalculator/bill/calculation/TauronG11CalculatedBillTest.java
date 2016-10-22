package pl.srw.billcalculator.bill.calculation;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        final TauronPrices prices = new TauronPrices(1L, "1.11", "2.02", "3.03", "4.04", "5.05", null, null, null, null, "0.00");

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

        assertThat(sut.getSellNetCharge(), is(new BigDecimal("11.10")));
        assertThat(sut.getSellVatCharge(), is(new BigDecimal("2.55")));
        assertThat(sut.getSellGrossCharge(), is(new BigDecimal("13.65")));
        assertThat(sut.getDistributeNetCharge(), is(new BigDecimal("44.44")));
        assertThat(sut.getDistributeVatCharge(), is(new BigDecimal("10.22")));
        assertThat(sut.getDistributeGrossCharge(), is(new BigDecimal("54.66")));
    }

    @Test
    public void whenAfterJuly16IncludeOplataOze() throws Exception {
        // prepare
        final TauronPrices prices = new TauronPrices(1L, "1.11", "2.02", "3.03", "4.04", "5.05", null, null, null, null, "0.00251");

        // calculate
        final TauronG11CalculatedBill sut = new TauronG11CalculatedBill(20, 30, "01/07/2016", "30/08/2016", prices);

        // verify
        assertThat(sut.getConsumptionFromJuly16(), is(10));
        assertThat(sut.getOplataOzeNetCharge(), is(new BigDecimal("0.02510")));
        assertThat(sut.getOplataOzeVatCharge(), is(new BigDecimal("0.0057730")));

        assertThat(sut.getNetChargeSum(), is(new BigDecimal("55.57")));
        assertThat(sut.getVatChargeSum(), is(new BigDecimal("12.78")));
        assertThat(sut.getGrossChargeSum(), is(new BigDecimal("68.35")));
        assertThat(sut.getExcise(), is(new BigDecimal("0.20")));

        assertThat(sut.getSellNetCharge(), is(new BigDecimal("11.10")));
        assertThat(sut.getSellVatCharge(), is(new BigDecimal("2.55")));
        assertThat(sut.getSellGrossCharge(), is(new BigDecimal("13.65")));
        assertThat(sut.getDistributeNetCharge(), is(new BigDecimal("44.47")));
        assertThat(sut.getDistributeVatCharge(), is(new BigDecimal("10.23")));
        assertThat(sut.getDistributeGrossCharge(), is(new BigDecimal("54.70")));
    }

    @Test
    public void realLifeExample() {
        // prepare
        final TauronPrices prices1 = new TauronPrices(1L, "0.25470", "0.18670", "1.46", "2.44", "0.80", null, null, null, null, "0.00");
        final TauronPrices prices2 = new TauronPrices(2L, "0.25680", "0.19130", "1.55", "3.29", "0.80", null, null, null, null, "0.00");

        // calculate
        final TauronG11CalculatedBill bill1 = new TauronG11CalculatedBill(7869, 8681, "01/08/2014", "31/12/2014", prices1);
        final TauronG11CalculatedBill bill2 = new TauronG11CalculatedBill(8681, 8865, "01/01/2015", "28/02/2015", prices2);

        // verify
        assertThat(bill1.getConsumption(), is(812));
        assertThat(bill2.getConsumption(), is(184));
        assertThat(bill1.getMonthCount(), is(5));
        assertThat(bill2.getMonthCount(), is(2));

        assertThat(round(bill1.getEnergiaElektrycznaNetCharge()), is(new BigDecimal("206.82")));
        assertThat(round(bill2.getEnergiaElektrycznaNetCharge()), is(new BigDecimal("47.25")));
        assertThat(round(bill1.getOplataDystrybucyjnaZmiennaNetCharge()), is(new BigDecimal("151.60")));
        assertThat(round(bill2.getOplataDystrybucyjnaZmiennaNetCharge()), is(new BigDecimal("35.20")));
        assertThat(round(bill1.getOplataDystrybucyjnaStalaNetCharge()), is(new BigDecimal("7.30")));
        assertThat(round(bill2.getOplataDystrybucyjnaStalaNetCharge()), is(new BigDecimal("3.10")));
        assertThat(round(bill1.getOplataPrzejsciowaNetCharge()), is(new BigDecimal("12.20")));
        assertThat(round(bill2.getOplataPrzejsciowaNetCharge()), is(new BigDecimal("6.58")));
        assertThat(round(bill1.getOplataAbonamentowaNetCharge().add(bill2.getOplataAbonamentowaNetCharge())), is(new BigDecimal("5.60")));

        assertThat(bill1.getSellNetCharge().add(bill2.getSellNetCharge()), is(new BigDecimal("254.07")));
        assertThat(bill1.getSellVatCharge().add(bill2.getSellVatCharge()), is(new BigDecimal("58.44")));
        assertThat(bill1.getSellGrossCharge().add(bill2.getSellGrossCharge()), is(new BigDecimal("312.51")));
        assertThat(bill1.getDistributeNetCharge().add(bill2.getDistributeNetCharge()), is(new BigDecimal("221.58")));
        assertThat(bill1.getDistributeVatCharge().add(bill2.getDistributeVatCharge()), is(new BigDecimal("50.96")));
        assertThat(bill1.getDistributeGrossCharge().add(bill2.getDistributeGrossCharge()), is(new BigDecimal("272.54")));

        assertThat(bill1.getNetChargeSum().add(bill2.getNetChargeSum()), is(new BigDecimal("475.65")));
        assertThat(bill1.getVatChargeSum().add(bill2.getVatChargeSum()), is(new BigDecimal("109.40")));
        assertThat(bill1.getGrossChargeSum().add(bill2.getGrossChargeSum()), is(new BigDecimal("585.05")));
        assertThat(bill1.getExcise().add(bill2.getExcise()), is(new BigDecimal("19.92")));
    }

    private BigDecimal round(BigDecimal val) {
        return val.setScale(2, RoundingMode.HALF_UP);
    }
}
