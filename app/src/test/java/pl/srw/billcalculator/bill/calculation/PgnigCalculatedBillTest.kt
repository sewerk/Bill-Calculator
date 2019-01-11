package pl.srw.billcalculator.bill.calculation

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import pl.srw.billcalculator.db.PgnigPrices
import java.math.BigDecimal
import java.math.BigInteger

@SuppressWarnings("LongMethod")
class PgnigCalculatedBillTest {

    @Test
    fun shouldProperlyCalculateBill() {
        // prepare
        val prices = PgnigPrices(
            1L,
            "1.01",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "0.00"
        )

        // calculate
        val sut = PgnigCalculatedBill(
            100,
            200,
            LocalDate.of(2015, Month.JULY, 31),
            LocalDate.of(2015, Month.SEPTEMBER, 24),
            prices
        )

        // verify
        assertThat(sut.consumptionM3, `is`(100))
        assertThat(sut.consumptionKWh, `is`(BigInteger.valueOf(505)))
        assertThat(sut.monthCount, `is`(2))
        assertThat(sut.monthCountExact, `is`(BigDecimal("1.8000")))

        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("2.02")))
        assertThat(sut.paliwoGazoweNetCharge, `is`(BigDecimal("1020.10")))
        assertThat(sut.dystrybucyjnaStalaNetCharge, `is`(BigDecimal("5.454000")))
        assertThat(sut.dystrybucyjnaZmiennaNetCharge, `is`(BigDecimal("2040.20")))

        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("0.4646")))
        assertThat(sut.paliwoGazoweVatCharge, `is`(BigDecimal("234.6230")))
        assertThat(sut.dystrybucyjnaStalaVatCharge, `is`(BigDecimal("1.25442000")))
        assertThat(sut.dystrybucyjnaZmiennaVatCharge, `is`(BigDecimal("469.2460")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("3067.77")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("705.58")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("3773.35")))
    }

    @Test
    fun shouldProperlyCalculateBillWithOplataHandlowa() {
        // prepare
        val prices = PgnigPrices(
            1L,
            "1.01",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "6.06"
        )

        // calculate
        val sut = PgnigCalculatedBill(
            100,
            200,
            LocalDate.of(2015, Month.JULY, 31),
            LocalDate.of(2015, Month.SEPTEMBER, 24),
            prices
        )

        // verify
        assertThat(sut.consumptionM3, `is`(100))
        assertThat(sut.consumptionKWh, `is`(BigInteger.valueOf(505)))
        assertThat(sut.monthCount, `is`(2))
        assertThat(sut.monthCountExact, `is`(BigDecimal("1.8000")))

        assertThat(sut.oplataAbonamentowaNetCharge, `is`(BigDecimal("2.02")))
        assertThat(sut.paliwoGazoweNetCharge, `is`(BigDecimal("1020.10")))
        assertThat(sut.dystrybucyjnaStalaNetCharge, `is`(BigDecimal("5.454000")))
        assertThat(sut.dystrybucyjnaZmiennaNetCharge, `is`(BigDecimal("2040.20")))
        assertThat(sut.oplataHandlowaNetCharge, `is`(BigDecimal("12.12")))

        assertThat(sut.oplataAbonamentowaVatCharge, `is`(BigDecimal("0.4646")))
        assertThat(sut.paliwoGazoweVatCharge, `is`(BigDecimal("234.6230")))
        assertThat(sut.dystrybucyjnaStalaVatCharge, `is`(BigDecimal("1.25442000")))
        assertThat(sut.dystrybucyjnaZmiennaVatCharge, `is`(BigDecimal("469.2460")))
        assertThat(sut.oplataHandlowaVatCharge, `is`(BigDecimal("2.7876")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("3079.89")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("708.37")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("3788.26")))
    }

    @Test
    fun shouldProperlyCalculateBillWithOplataHandlowaDisabledInDb() {
        // prepare
        val prices = PgnigPrices(
            1L,
            "1.01",
            "2.02",
            "3.03",
            "4.04",
            "5.05",
            "-1"
        )

        // calculate
        val sut = PgnigCalculatedBill(
            100,
            200,
            LocalDate.of(2015, Month.JULY, 31),
            LocalDate.of(2015, Month.SEPTEMBER, 24),
            prices
        )

        // verify
        assertThat(sut.consumptionM3, `is`(100))
        assertThat(sut.consumptionKWh, `is`(BigInteger.valueOf(505)))
        assertThat(sut.monthCount, `is`(2))
        assertThat(sut.monthCountExact, `is`(BigDecimal("1.8000")))

        assertThat(sut.oplataHandlowaNetCharge, `is`(BigDecimal("0")))
        assertThat(sut.oplataHandlowaVatCharge, `is`(BigDecimal("0.00")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("3067.77")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("705.58")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("3773.35")))
    }

    @Test
    fun shouldProperlyCalculateBillWithOplataHandlowaDisabledInPreferences() {
        // prepare
        val prices: pl.srw.billcalculator.settings.prices.PgnigPrices = mock {
            on { oplataAbonamentowa } doReturn "1.01"
            on { paliwoGazowe } doReturn "2.02"
            on { dystrybucyjnaStala } doReturn "3.03"
            on { dystrybucyjnaZmienna } doReturn "4.04"
            on { wspolczynnikKonwersji } doReturn "5.05"
            on { oplataHandlowa } doReturn "6.06"
            on { enabledOplataHandlowa } doReturn false
        }

        // calculate
        val sut = PgnigCalculatedBill(
            100,
            200,
            LocalDate.of(2015, Month.JULY, 31),
            LocalDate.of(2015, Month.SEPTEMBER, 24),
            prices
        )

        // verify
        assertThat(sut.consumptionM3, `is`(100))
        assertThat(sut.consumptionKWh, `is`(BigInteger.valueOf(505)))
        assertThat(sut.monthCount, `is`(2))
        assertThat(sut.monthCountExact, `is`(BigDecimal("1.8000")))

        assertThat(sut.oplataHandlowaNetCharge, `is`(BigDecimal("0")))
        assertThat(sut.oplataHandlowaVatCharge, `is`(BigDecimal("0.00")))

        assertThat(sut.netChargeSum, `is`(BigDecimal("3067.77")))
        assertThat(sut.vatChargeSum, `is`(BigDecimal("705.58")))
        assertThat(sut.grossChargeSum, `is`(BigDecimal("3773.35")))
    }
}
