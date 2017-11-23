@file:Suppress("IllegalIdentifier")
package pl.srw.billcalculator.form

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.threeten.bp.LocalDate
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.wrapper.PricesRepo
import pl.srw.billcalculator.wrapper.ReadingsRepo

class FormVMTest {

    @Rule @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    val readingsRepo = mock<ReadingsRepo>()
    val pricesRepo = mock<PricesRepo>()
    val tariffLiveData = MutableLiveData<String>()

    lateinit var sut: FormVM

    @Before
    fun setUp() {
        whenever(pricesRepo.tariffPge).thenReturn(tariffLiveData)
        whenever(pricesRepo.tariffTauron).thenReturn(tariffLiveData)
        sut = FormVM(readingsRepo, pricesRepo)
    }

    @Test
    fun `initialize fromDate with first day of current month`() {
        assertEquals(calculateDateFrom(), sut.fromDate)
    }

    @Test
    fun `initialize toDate with last dat of current month`() {
        assertEquals(calculateDateTo(), sut.toDate)
    }

    @Test
    fun `fetches single previous readings for PGNIG provider`() {
        val readings = `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGNIG)

        assertEquals(readings.value, sut.singlePrevReadings.value)
    }

    @Test
    fun `does not fetch double previous readings for PGNIG provider`() {
        `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGNIG)

        assertTrue(sut.dayPrevReadings.value!!.isEmpty())
        assertTrue(sut.nightPrevReadings.value!!.isEmpty())
    }

    @Test
    fun `fetches single previous readings for PGE with G11 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        val readings = `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGE)

        assertEquals(readings.value, sut.singlePrevReadings.value)
    }

    @Test
    fun `does not fetch double previous readings for PGE with G11 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGE)

        assertTrue(sut.dayPrevReadings.value!!.isEmpty())
        assertTrue(sut.nightPrevReadings.value!!.isEmpty())
    }

    @Test
    fun `fetches double previous readings for PGE with G12 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)
        val readings = `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGE)

        assertEquals(readings.value, sut.dayPrevReadings.value)
        assertEquals(readings.value, sut.nightPrevReadings.value)
    }

    @Test
    fun `does not fetch single previous readings for PGE with G12 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)
        `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGE)

        assertTrue(sut.singlePrevReadings.value!!.isEmpty())
    }

    @Test
    fun `fetches single previous readings for TAURON with G11 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        val readings = `given getPreviousReading for any type, returns`()

        sut.init(Provider.TAURON)

        assertEquals(readings.value, sut.singlePrevReadings.value)
    }

    @Test
    fun `does not fetch double previous readings for TAURON with G11 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        `given getPreviousReading for any type, returns`()

        sut.init(Provider.TAURON)

        assertTrue(sut.dayPrevReadings.value!!.isEmpty())
        assertTrue(sut.nightPrevReadings.value!!.isEmpty())
    }

    @Test
    fun `fetches double previous readings for TAURON with G12 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)
        val readings = `given getPreviousReading for any type, returns`()

        sut.init(Provider.TAURON)

        assertEquals(readings.value, sut.dayPrevReadings.value)
        assertEquals(readings.value, sut.nightPrevReadings.value)
    }

    @Test
    fun `does not fetch single previous readings for TAURON with G12 tariff`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)
        `given getPreviousReading for any type, returns`()

        sut.init(Provider.TAURON)

        assertTrue(sut.singlePrevReadings.value!!.isEmpty())
    }

    @Test
    fun `updates previous readings when tariff changes`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        val readings = `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGE)
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        assertEquals(readings.value, sut.dayPrevReadings.value)
        assertEquals(readings.value, sut.nightPrevReadings.value)
        assertTrue(sut.singlePrevReadings.value!!.isEmpty())
    }

    @Test
    fun `does not update previous readings when view re-attached`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        `given getPreviousReading for any type, returns`()

        sut.init(Provider.PGE)
        reset(readingsRepo)
        sut.init(Provider.PGE)

        verify(readingsRepo, never()).getPreviousReadingsFor(any())
    }

    private fun calculateDateFrom(): LocalDate = LocalDate.now().withDayOfMonth(1)

    private fun calculateDateTo(): LocalDate {
        val now = LocalDate.now()
        return now.withDayOfMonth(now.lengthOfMonth())
    }

    private fun given_tariff(@SharedPreferencesEnergyPrices.TariffOption tariff: String) {
        tariffLiveData.value = tariff
        whenever(pricesRepo.getTariff(any())).thenReturn(tariff)
    }

    private fun `given getPreviousReading for any type, returns`(): LiveData<IntArray> {
        val readings: LiveData<IntArray> = mock()
        whenever(readings.value).thenReturn(intArrayOf(1, 2, 3))
        whenever(readingsRepo.getPreviousReadingsFor(any())).thenReturn(readings)
        return readings
    }
}