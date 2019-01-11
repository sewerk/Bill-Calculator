package pl.srw.billcalculator.bill.calculation

import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

import java.math.BigDecimal

import pl.srw.billcalculator.db.PgePrices

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

@SuppressWarnings("LongMethod")
class PgeG12CalculatedBillTest {

    @Test
    fun shouldCalculateBillWithOplataHandlowaDisabled() {
        // prepare
        val prices = PgePrices(
            1L,
            null,
            "2.02",
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
        val sut = PgeG12CalculatedBill(
            20,
            30,
            35,
            55,
            LocalDate.of(2015, Month.JANUARY, 1),
            LocalDate.of(2015, Month.OCTOBER, 30),
            prices
        )

        // verify
        assertThat(sut.dayConsumption, `is`(10))
        assertThat(sut.nightConsumption, `is`(20))
        assertThat(sut.totalConsumption, `is`(30))
        assertThat(sut.monthCount, `is`(10))

        assertThat(sut.zaEnergieCzynnaDayNetCharge, `is`(BigDecimal("70.70")))
        assertThat(sut.skladnikJakosciowyDayNetCharge, `is`(BigDecimal("20.20")))
        assertThat(sut.oplataSieciowaDayNetCharge, `is`(BigDecimal("90.90")))
        assertThat(sut.zaEnergieCzynnaNightNetCharge, `is`(BigDecimal("161.60")))
        assertThat(sut.skladnikJakosciowyNightNetCharge, `is`(BigDecimal("40.40")))
        assertThat(sut.oplataSieciowaNightNetCharge, `is`(BigDecimal("222.20")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("40.40")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("50.50")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("60.60")))

        assertThat(sut.zaEnergieCzynnaDayVatCharge, `is`(BigDecimal("16.2610")))
        assertThat(sut.skladnikJakosciowyDayVatCharge, `is`(BigDecimal("4.6460")))
        assertThat(sut.oplataSieciowaDayVatCharge, `is`(BigDecimal("20.9070")))
        assertThat(sut.zaEnergieCzynnaNightVatCharge, `is`(BigDecimal("37.1680")))
        assertThat(sut.skladnikJakosciowyNightVatCharge, `is`(BigDecimal("9.2920")))
        assertThat(sut.oplataSieciowaNightVatCharge, `is`(BigDecimal("51.1060")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("9.2920")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("11.6150")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("13.9380")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("757.50")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("174.23")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("931.73")))
        assertThat(sut.excise, `is`(BigDecimal("0.60")))
    }

    @Test
    fun shouldCalculateBillWithOplataHandlowa() {
        // prepare
        val prices = PgePrices(
            1L,
            null,
            "2.02",
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
        val sut = PgeG12CalculatedBill(
            20,
            30,
            35,
            55,
            LocalDate.of(2015, Month.JANUARY, 1),
            LocalDate.of(2015, Month.OCTOBER, 30),
            prices
        )

        // verify
        assertThat(sut.dayConsumption, `is`(10))
        assertThat(sut.nightConsumption, `is`(20))
        assertThat(sut.totalConsumption, `is`(30))
        assertThat(sut.monthCount, `is`(10))

        assertThat(sut.zaEnergieCzynnaDayNetCharge, `is`(BigDecimal("70.70")))
        assertThat(sut.skladnikJakosciowyDayNetCharge, `is`(BigDecimal("20.20")))
        assertThat(sut.oplataSieciowaDayNetCharge, `is`(BigDecimal("90.90")))
        assertThat(sut.zaEnergieCzynnaNightNetCharge, `is`(BigDecimal("161.60")))
        assertThat(sut.skladnikJakosciowyNightNetCharge, `is`(BigDecimal("40.40")))
        assertThat(sut.oplataSieciowaNightNetCharge, `is`(BigDecimal("222.20")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("40.40")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("50.50")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("60.60")))
        assertThat(sut.oplataHandlowaNetCharge, `is`(BigDecimal("121.20")))

        assertThat(sut.zaEnergieCzynnaDayVatCharge, `is`(BigDecimal("16.2610")))
        assertThat(sut.skladnikJakosciowyDayVatCharge, `is`(BigDecimal("4.6460")))
        assertThat(sut.oplataSieciowaDayVatCharge, `is`(BigDecimal("20.9070")))
        assertThat(sut.zaEnergieCzynnaNightVatCharge, `is`(BigDecimal("37.1680")))
        assertThat(sut.skladnikJakosciowyNightVatCharge, `is`(BigDecimal("9.2920")))
        assertThat(sut.oplataSieciowaNightVatCharge, `is`(BigDecimal("51.1060")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("9.2920")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("11.6150")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("13.9380")))
        assertThat(sut.oplataHandlowaVatCharge, `is`(BigDecimal("27.8760")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("878.70")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("202.10")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("1080.80")))
        assertThat(sut.excise, `is`(BigDecimal("0.60")))
    }

    @Test
    fun whenAfterJuly16IncludeOplataOze() {
        // prepare
        val prices = PgePrices(
            1L,
            null,
            "2.02",
            null,
            "4.04",
            "5.05",
            "6.06",
            "0.00",
            "7.07",
            "8.08",
            "9.09",
            "11.11",
            "2.51"
        )

        // calculate
        val sut = PgeG12CalculatedBill(
            20,
            30,
            35,
            55,
            LocalDate.of(2016, Month.JULY, 1),
            LocalDate.of(2017, Month.APRIL, 30),
            prices
        )

        // verify
        assertThat(sut.dayConsumptionFromJuly16, `is`(10))
        assertThat(sut.nightConsumptionFromJuly16, `is`(20))

        assertThat(sut.oplataOzeDayNetCharge, `is`(BigDecimal("0.0251")))
        assertThat(sut.oplataOzeNightNetCharge, `is`(BigDecimal("0.0502")))
        assertThat(sut.oplataOzeDayVatCharge, `is`(BigDecimal("0.005773")))
        assertThat(sut.oplataOzeNightVatCharge, `is`(BigDecimal("0.011546")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("757.58")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("174.24")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("931.82")))
        assertThat(sut.excise, `is`(BigDecimal("0.60")))
    }
}
