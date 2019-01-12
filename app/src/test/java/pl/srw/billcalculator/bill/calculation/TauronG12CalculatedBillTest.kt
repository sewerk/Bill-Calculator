package pl.srw.billcalculator.bill.calculation

import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

import java.math.BigDecimal

import pl.srw.billcalculator.db.TauronPrices

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

@SuppressWarnings("LongMethod")
class TauronG12CalculatedBillTest {

    @Test
    fun shouldCalculateBillWithOplataHandlowaDisabled() {
        // prepare
        val prices = TauronPrices(
            1L,
            null,
            null,
            "4.04",
            "5.05",
            "6.06",
            "-1",
            "7.07",
            "8.08",
            "9.09",
            "11.11",
            "0.00"
        )

        // calculate
        val sut = TauronG12CalculatedBill(
            11,
            21,
            25,
            45,
            LocalDate.of(2015, Month.JANUARY, 1),
            LocalDate.of(2015, Month.OCTOBER, 30),
            prices
        )

        // verify
        assertThat(sut.dayConsumption, `is`(10))
        assertThat(sut.nightConsumption, `is`(20))
        assertThat(sut.totalConsumption, `is`(30))
        assertThat(sut.monthCount, `is`(10))

        assertThat(sut.energiaElektrycznaDayNetCharge, `is`(BigDecimal("70.70")))
        assertThat(sut.oplataDystrybucyjnaZmiennaDayNetCharge, `is`(BigDecimal("90.90")))
        assertThat(sut.energiaElektrycznaNightNetCharge, `is`(BigDecimal("161.60")))
        assertThat(sut.oplataDystrybucyjnaZmiennaNightNetCharge, `is`(BigDecimal("222.20")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("50.50")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("40.40")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("60.60")))

        assertThat(sut.energiaElektrycznaDayVatCharge, `is`(BigDecimal("16.2610")))
        assertThat(sut.oplataDystrybucyjnaZmiennaDayVatCharge, `is`(BigDecimal("20.9070")))
        assertThat(sut.energiaElektrycznaNightVatCharge, `is`(BigDecimal("37.1680")))
        assertThat(sut.oplataDystrybucyjnaZmiennaNightVatCharge, `is`(BigDecimal("51.1060")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("11.6150")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("9.2920")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("13.9380")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("696.90")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("160.29")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("857.19")))
        assertThat(sut.excise, `is`(BigDecimal("0.60")))

        assertThat(sut.sellNetCharge, `is`(BigDecimal("232.30")))
        assertThat(sut.sellVatCharge, `is`(BigDecimal("53.43")))
        assertThat(sut.sellGrossCharge, `is`(BigDecimal("285.73")))
        assertThat(sut.distributeNetCharge, `is`(BigDecimal("464.60")))
        assertThat(sut.distributeVatCharge, `is`(BigDecimal("106.86")))
        assertThat(sut.distributeGrossCharge, `is`(BigDecimal("571.46")))
    }

    @Test
    fun shouldCalculateBillWithOplataHandlowa() {
        // prepare
        val prices = TauronPrices(
            1L,
            null,
            null,
            "4.04",
            "5.05",
            "6.06",
            "12.12",
            "7.07",
            "8.08",
            "9.09",
            "11.11",
            "0.00"
        )

        // calculate
        val sut = TauronG12CalculatedBill(
            11,
            21,
            25,
            45,
            LocalDate.of(2015, Month.JANUARY, 1),
            LocalDate.of(2015, Month.OCTOBER, 30),
            prices
        )

        // verify
        assertThat(sut.dayConsumption, `is`(10))
        assertThat(sut.nightConsumption, `is`(20))
        assertThat(sut.totalConsumption, `is`(30))
        assertThat(sut.monthCount, `is`(10))

        assertThat(sut.energiaElektrycznaDayNetCharge, `is`(BigDecimal("70.70")))
        assertThat(sut.oplataDystrybucyjnaZmiennaDayNetCharge, `is`(BigDecimal("90.90")))
        assertThat(sut.energiaElektrycznaNightNetCharge, `is`(BigDecimal("161.60")))
        assertThat(sut.oplataDystrybucyjnaZmiennaNightNetCharge, `is`(BigDecimal("222.20")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("50.50")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("40.40")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("60.60")))
        assertThat(sut.oplataHandlowaNetCharge, `is`(BigDecimal("121.20")))

        assertThat(sut.energiaElektrycznaDayVatCharge, `is`(BigDecimal("16.2610")))
        assertThat(sut.oplataDystrybucyjnaZmiennaDayVatCharge, `is`(BigDecimal("20.9070")))
        assertThat(sut.energiaElektrycznaNightVatCharge, `is`(BigDecimal("37.1680")))
        assertThat(sut.oplataDystrybucyjnaZmiennaNightVatCharge, `is`(BigDecimal("51.1060")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("11.6150")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("9.2920")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("13.9380")))
        assertThat(sut.oplataHandlowaVatCharge, `is`(BigDecimal("27.8760")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("818.10")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("188.16")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("1006.26")))
        assertThat(sut.excise, `is`(BigDecimal("0.60")))

        assertThat(sut.sellNetCharge, `is`(BigDecimal("353.50")))
        assertThat(sut.sellVatCharge, `is`(BigDecimal("81.31")))
        assertThat(sut.sellGrossCharge, `is`(BigDecimal("434.81")))
        assertThat(sut.distributeNetCharge, `is`(BigDecimal("464.60")))
        assertThat(sut.distributeVatCharge, `is`(BigDecimal("106.85")))
        assertThat(sut.distributeGrossCharge, `is`(BigDecimal("571.45")))
    }

    @Test
    fun whenAfterJuly16IncludeOplataOze() {
        // prepare
        val prices = TauronPrices(
            1L,
            null,
            null,
            "4.04",
            "5.05",
            "6.06",
            "0.00",
            "7.07",
            "8.08",
            "9.09",
            "11.11",
            "0.00251"
        )

        // calculate
        val sut = TauronG12CalculatedBill(
            11,
            21,
            25,
            45,
            LocalDate.of(2016, Month.JULY, 1),
            LocalDate.of(2017, Month.APRIL, 30),
            prices
        )

        // verify
        assertThat(sut.dayConsumptionFromJuly16, `is`(10))
        assertThat(sut.nightConsumptionFromJuly16, `is`(20))

        assertThat(sut.oplataOzeDayNetCharge, `is`(BigDecimal("0.02510")))
        assertThat(sut.oplataOzeNightNetCharge, `is`(BigDecimal("0.05020")))
        assertThat(sut.oplataOzeDayVatCharge, `is`(BigDecimal("0.0057730")))
        assertThat(sut.oplataOzeNightVatCharge, `is`(BigDecimal("0.0115460")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("696.98")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("160.30")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("857.28")))
        assertThat(sut.excise, `is`(BigDecimal("0.60")))

        assertThat(sut.sellNetCharge, `is`(BigDecimal("232.30")))
        assertThat(sut.sellVatCharge, `is`(BigDecimal("53.43")))
        assertThat(sut.sellGrossCharge, `is`(BigDecimal("285.73")))
        assertThat(sut.distributeNetCharge, `is`(BigDecimal("464.68")))
        assertThat(sut.distributeVatCharge, `is`(BigDecimal("106.87")))
        assertThat(sut.distributeGrossCharge, `is`(BigDecimal("571.55")))
    }
}
