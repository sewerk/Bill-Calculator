package pl.srw.billcalculator.bill.calculation

import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

import java.math.BigDecimal

import pl.srw.billcalculator.db.PgePrices

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

@SuppressWarnings("LongMethod")
class PgeG11CalculatedBillTest {

    @Test
    fun shouldCalculateBillWithOplataHandlowaDisabled() {
        // prepare
        val prices = PgePrices(
            1L,
            "1.11",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "6.06",
            "-1",
            null,
            null,
            null,
            null,
            "0"
        )

        // calculate
        val sut = PgeG11CalculatedBill(
            20,
            30,
            LocalDate.of(2015, Month.MARCH, 1),
            LocalDate.of(2015, Month.APRIL, 30),
            prices
        )

        // verify
        assertThat(sut.totalConsumption, `is`(10))
        assertThat(sut.monthCount, `is`(2))

        assertThat(sut.zaEnergieCzynnaNetCharge, `is`(BigDecimal("11.10")))
        assertThat(sut.skladnikJakosciowyNetCharge, `is`(BigDecimal("20.20")))
        assertThat(sut.oplataSieciowaNetCharge, `is`(BigDecimal("30.30")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("8.08")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("10.10")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("12.12")))

        assertThat(sut.zaEnergieCzynnaVatCharge, `is`(BigDecimal("2.5530")))
        assertThat(sut.skladnikJakosciowyVatCharge, `is`(BigDecimal("4.6460")))
        assertThat(sut.oplataSieciowaVatCharge, `is`(BigDecimal("6.9690")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("1.8584")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("2.3230")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("2.7876")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("91.90")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("21.14")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("113.04")))
        assertThat(sut.excise, `is`(BigDecimal("0.20")))
    }

    @Test
    fun shouldCalculateBillWithOplataHandlowa() {
        // prepare
        val prices = PgePrices(
            1L,
            "1.11",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "6.06",
            "7.07",
            null,
            null,
            null,
            null,
            "0"
        )

        // calculate
        val sut = PgeG11CalculatedBill(
            20,
            30,
            LocalDate.of(2015, Month.MARCH, 1),
            LocalDate.of(2015, Month.APRIL, 30),
            prices
        )

        // verify
        assertThat(sut.totalConsumption, `is`(10))
        assertThat(sut.monthCount, `is`(2))

        assertThat(sut.zaEnergieCzynnaNetCharge, `is`(BigDecimal("11.10")))
        assertThat(sut.skladnikJakosciowyNetCharge, `is`(BigDecimal("20.20")))
        assertThat(sut.oplataSieciowaNetCharge, `is`(BigDecimal("30.30")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("8.08")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("10.10")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("12.12")))
        assertThat(sut.oplataHandlowaNetCharge, `is`(BigDecimal("14.14")))

        assertThat(sut.zaEnergieCzynnaVatCharge, `is`(BigDecimal("2.5530")))
        assertThat(sut.skladnikJakosciowyVatCharge, `is`(BigDecimal("4.6460")))
        assertThat(sut.oplataSieciowaVatCharge, `is`(BigDecimal("6.9690")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("1.8584")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("2.3230")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("2.7876")))
        assertThat(sut.oplataHandlowaVatCharge, `is`(BigDecimal("3.2522")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("106.04")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("24.39")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("130.43")))
        assertThat(sut.excise, `is`(BigDecimal("0.20")))
    }

    @Test
    fun whenAfterJuly16IncludeOplataOze() {
        // GIVEN
        val prices = PgePrices(
            1L,
            "1.11",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "6.06",
            "0.00",
            null,
            null,
            null,
            null,
            "2.51"
        )

        // WHEN
        val sut = PgeG11CalculatedBill(
            20,
            30,
            LocalDate.of(2016, Month.JULY, 1),
            LocalDate.of(2016, Month.AUGUST, 30),
            prices
        )

        // THEN
        assertThat(sut.consumptionFromJuly16, `is`(10))
        assertThat(sut.oplataOzeNetCharge, `is`(BigDecimal("0.0251")))
        assertThat(sut.oplataOzeVatCharge, `is`(BigDecimal("0.005773")))
        assertThat(sut.netChargeSum, `is`(BigDecimal("91.93")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("21.14")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("113.07")))
        assertThat(sut.excise, `is`(BigDecimal("0.20")))
    }
}
