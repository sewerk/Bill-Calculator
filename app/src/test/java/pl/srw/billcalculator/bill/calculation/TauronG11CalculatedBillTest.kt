package pl.srw.billcalculator.bill.calculation

import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

import java.math.BigDecimal
import java.math.RoundingMode

import pl.srw.billcalculator.db.TauronPrices

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

@SuppressWarnings("LongMethod")
class TauronG11CalculatedBillTest {

    @Test
    fun shouldCalculateBillWithOplataHandlowaDisabled() {
        // prepare
        val prices = TauronPrices(
            1L,
            "1.11",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "-1",
            null,
            null,
            null,
            null,
            "0.00"
        )

        // calculate
        val sut = TauronG11CalculatedBill(
            20,
            30,
            LocalDate.of(2015, Month.MARCH, 1),
            LocalDate.of(2015, Month.APRIL, 30),
            prices
        )

        // verify
        assertThat(sut.totalConsumption, `is`(10))
        assertThat(sut.monthCount, `is`(2))

        assertThat(sut.energiaElektrycznaNetCharge, `is`(BigDecimal("11.10")))
        assertThat(sut.oplataDystrybucyjnaZmiennaNetCharge, `is`(BigDecimal("20.20")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("6.06")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("8.08")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("10.10")))

        assertThat(sut.energiaElektrycznaVatCharge, `is`(BigDecimal("2.5530")))
        assertThat(sut.oplataDystrybucyjnaZmiennaVatCharge, `is`(BigDecimal("4.6460")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("1.3938")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("1.8584")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("2.3230")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("55.54")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("12.77")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("68.31")))
        assertThat(sut.excise, `is`(BigDecimal("0.20")))

        assertThat(sut.sellNetCharge, `is`(BigDecimal("11.10")))
        assertThat(sut.sellVatCharge, `is`(BigDecimal("2.55")))
        assertThat(sut.sellGrossCharge, `is`(BigDecimal("13.65")))
        assertThat(sut.distributeNetCharge, `is`(BigDecimal("44.44")))
        assertThat(sut.distributeVatCharge, `is`(BigDecimal("10.22")))
        assertThat(sut.distributeGrossCharge, `is`(BigDecimal("54.66")))
    }

    @Test
    fun shouldCalculateBillWithOplataHandlowa() {
        // prepare
        val prices = TauronPrices(
            1L,
            "1.11",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "6.06",
            null,
            null,
            null,
            null,
            "0.00"
        )

        // calculate
        val sut = TauronG11CalculatedBill(
            20,
            30,
            LocalDate.of(2015, Month.MARCH, 1),
            LocalDate.of(2015, Month.APRIL, 30),
            prices
        )

        // verify
        assertThat(sut.totalConsumption, `is`(10))
        assertThat(sut.monthCount, `is`(2))

        assertThat(sut.energiaElektrycznaNetCharge, `is`(BigDecimal("11.10")))
        assertThat(sut.oplataDystrybucyjnaZmiennaNetCharge, `is`(BigDecimal("20.20")))
        assertThat(sut.oplataDystrybucyjnaStalaNetCharge, `is`(BigDecimal("6.06")))
        assertThat(sut.oplataPrzejsciowaNetCharge, `is`(BigDecimal("8.08")))
        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("10.10")))
        assertThat(sut.oplataHandlowaNetCharge, `is`(BigDecimal("12.12")))

        assertThat(sut.energiaElektrycznaVatCharge, `is`(BigDecimal("2.5530")))
        assertThat(sut.oplataDystrybucyjnaZmiennaVatCharge, `is`(BigDecimal("4.6460")))
        assertThat(sut.oplataDystrybucyjnaStalaVatCharge, `is`(BigDecimal("1.3938")))
        assertThat(sut.oplataPrzejsciowaVatCharge, `is`(BigDecimal("1.8584")))
        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("2.3230")))
        assertThat(sut.oplataHandlowaVatCharge, `is`(BigDecimal("2.7876")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("67.66")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("15.56")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("83.22")))
        assertThat(sut.excise, `is`(BigDecimal("0.20")))

        assertThat(sut.sellNetCharge, `is`(BigDecimal("23.22")))
        assertThat(sut.sellVatCharge, `is`(BigDecimal("5.34")))
        assertThat(sut.sellGrossCharge, `is`(BigDecimal("28.56")))
        assertThat(sut.distributeNetCharge, `is`(BigDecimal("44.44")))
        assertThat(sut.distributeVatCharge, `is`(BigDecimal("10.22")))
        assertThat(sut.distributeGrossCharge, `is`(BigDecimal("54.66")))
    }

    @Test
    fun whenAfterJuly16IncludeOplataOze() {
        // prepare
        val prices = TauronPrices(
            1L,
            "1.11",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "0.00",
            null,
            null,
            null,
            null,
            "0.00251"
        )

        // calculate
        val sut = TauronG11CalculatedBill(
            20,
            30,
            LocalDate.of(2016, Month.JULY, 1),
            LocalDate.of(2016, Month.AUGUST, 30),
            prices
        )

        // verify
        assertThat(sut.consumptionFromJuly16, `is`(10))
        assertThat(sut.oplataOzeNetCharge, `is`(BigDecimal("0.02510")))
        assertThat(sut.oplataOzeVatCharge, `is`(BigDecimal("0.0057730")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("55.57")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("12.78")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("68.35")))
        assertThat(sut.excise, `is`(BigDecimal("0.20")))

        assertThat(sut.sellNetCharge, `is`(BigDecimal("11.10")))
        assertThat(sut.sellVatCharge, `is`(BigDecimal("2.55")))
        assertThat(sut.sellGrossCharge, `is`(BigDecimal("13.65")))
        assertThat(sut.distributeNetCharge, `is`(BigDecimal("44.47")))
        assertThat(sut.distributeVatCharge, `is`(BigDecimal("10.23")))
        assertThat(sut.distributeGrossCharge, `is`(BigDecimal("54.70")))
    }

    @Test
    fun realLifeExample() {
        // prepare
        val prices1 = TauronPrices(
            1L,
            "0.25470",
            "0.18670",
            "1.46",
            "2.44",
            "0.80",
            "-1",
            null,
            null,
            null,
            null,
            "0.00"
        )
        val prices2 = TauronPrices(
            2L,
            "0.25680",
            "0.19130",
            "1.55",
            "3.29",
            "0.80",
            "-1",
            null,
            null,
            null,
            null,
            "0.00"
        )

        // calculate
        val bill1 = TauronG11CalculatedBill(
                7869,
                8681,
                LocalDate.of(2014, Month.AUGUST, 1),
                LocalDate.of(2014, Month.DECEMBER, 31),
                prices1
            )
        val bill2 = TauronG11CalculatedBill(
            8681,
            8865,
            LocalDate.of(2015, Month.JANUARY, 1),
            LocalDate.of(2015, Month.FEBRUARY, 28),
            prices2
        )

        // verify
        assertThat(bill1.totalConsumption, `is`(812))
        assertThat(bill2.totalConsumption, `is`(184))
        assertThat(bill1.monthCount, `is`(5))
        assertThat(bill2.monthCount, `is`(2))

        assertThat(round(bill1.energiaElektrycznaNetCharge), `is`(BigDecimal("206.82")))
        assertThat(round(bill2.energiaElektrycznaNetCharge), `is`(BigDecimal("47.25")))
        assertThat(round(bill1.oplataDystrybucyjnaZmiennaNetCharge), `is`(BigDecimal("151.60")))
        assertThat(round(bill2.oplataDystrybucyjnaZmiennaNetCharge), `is`(BigDecimal("35.20")))
        assertThat(round(bill1.oplataDystrybucyjnaStalaNetCharge), `is`(BigDecimal("7.30")))
        assertThat(round(bill2.oplataDystrybucyjnaStalaNetCharge), `is`(BigDecimal("3.10")))
        assertThat(round(bill1.oplataPrzejsciowaNetCharge), `is`(BigDecimal("12.20")))
        assertThat(round(bill2.oplataPrzejsciowaNetCharge), `is`(BigDecimal("6.58")))
        assertThat(round(bill1.oplataAbonamentowaNetCharge.add(bill2.oplataAbonamentowaNetCharge)), `is`(BigDecimal("5.60")))

        assertThat(bill1.sellNetCharge.add(bill2.sellNetCharge), `is`(BigDecimal("254.07")))
        assertThat(bill1.sellVatCharge.add(bill2.sellVatCharge), `is`(BigDecimal("58.44")))
        assertThat(bill1.sellGrossCharge.add(bill2.sellGrossCharge), `is`(BigDecimal("312.51")))
        assertThat(bill1.distributeNetCharge.add(bill2.distributeNetCharge), `is`(BigDecimal("221.58")))
        assertThat(bill1.distributeVatCharge.add(bill2.distributeVatCharge), `is`(BigDecimal("50.96")))
        assertThat(bill1.distributeGrossCharge.add(bill2.distributeGrossCharge), `is`(BigDecimal("272.54")))

        assertThat(bill1.netChargeSum.add(bill2.netChargeSum), `is`(BigDecimal("475.65")))
        assertThat(bill1.vatChargeSum.add(bill2.vatChargeSum), `is`(BigDecimal("109.40")))
        assertThat(bill1.grossChargeSum.add(bill2.grossChargeSum), `is`(BigDecimal("585.05")))
        assertThat(bill1.excise.add(bill2.excise), `is`(BigDecimal("19.92")))
    }

    private fun round(value: BigDecimal) = value.setScale(2, RoundingMode.HALF_UP)
}
