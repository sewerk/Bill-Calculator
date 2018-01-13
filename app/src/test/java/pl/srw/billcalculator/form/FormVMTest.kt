package pl.srw.billcalculator.form

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.view.View
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates

@RunWith(JUnitParamsRunner::class)
class FormVMTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val provider = Provider.PGE
    val tariffLiveData = MutableLiveData<EnergyTariff>()
    val pricesRepo: PricesRepo = mock {
        on { tariffPge } doReturn tariffLiveData
        on { tariffTauron } doReturn tariffLiveData
    }
    var sut = FormVM(provider, pricesRepo)

    @Test
    fun `initialize fromDate with first day of current month`() {
        assertEquals(calculateDateFrom(), sut.dateFrom)
    }

    @Test
    fun `initialize toDate with last dat of current month`() {
        assertEquals(calculateDateTo(), sut.dateTo)
    }

    @Test
    fun `initialize provider logo with given provider`() {
        assertEquals(provider.logoRes, sut.logoResource)
    }

    @Test
    fun `sets readings unit for given provider`() {
        assertEquals(provider.formReadingUnit, sut.readingsUnitTextResource)
    }

    @Test
    @Parameters("G11", "G12")
    fun `sets tariff label for energy providers`(tariff: EnergyTariff) {
        setTariff(tariff)

        assertEquals(tariff.name, sut.tariffLabel)
    }

    @Test
    fun `sets empty tariff label for PGNIG`() {
        sut = FormVM(Provider.PGNIG, pricesRepo)

        assertEquals("", sut.tariffLabel)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun `single readings are visible when tariff G11`(provider: Provider) {
        sut = FormVM(provider, pricesRepo)
        setTariff(EnergyTariff.G11)

        assertEquals(View.VISIBLE, sut.singleReadingsVisibility)
        assertEquals(View.GONE, sut.doubleReadingsVisibility)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun `double readings are visible when tariff G12`(provider: Provider) {
        sut = FormVM(provider, pricesRepo)
        setTariff(EnergyTariff.G12)

        assertEquals(View.GONE, sut.singleReadingsVisibility)
        assertEquals(View.VISIBLE, sut.doubleReadingsVisibility)
    }

    @Test
    fun `single readings are visible when PGNIG provider`() {
        sut = FormVM(Provider.PGNIG, pricesRepo)

        assertEquals(View.VISIBLE, sut.singleReadingsVisibility)
        assertEquals(View.GONE, sut.doubleReadingsVisibility)
    }

    @Test
    fun `opens settings screen for provider when settings linked clicked`() {
        val observer:Observer<Provider?> = mock()
        sut.openSettingsCommand.observeForever(observer)

        sut.settingsLinkClicked()

        verify(observer).onChanged(provider)
    }

    @Test
    fun `is single readings processing when tariff G11`() {
        setTariff(EnergyTariff.G11)

        assertTrue(sut.isSingleReadingsProcessing())
    }

    @Test
    fun `is not single readings processing when tariff G12`() {
        setTariff(EnergyTariff.G12)

        assertFalse(sut.isSingleReadingsProcessing())
    }

    @Test
    fun `is single readings processing for PGNiG provider`() {
        sut = FormVM(Provider.PGNIG, pricesRepo)

        assertTrue(sut.isSingleReadingsProcessing())
    }

    private fun calculateDateFrom() = Dates.format(LocalDate.now().withDayOfMonth(1), FormFragment.DATE_PATTERN)

    private fun calculateDateTo(): String {
        val now = LocalDate.now()
        return Dates.format(now.withDayOfMonth(now.lengthOfMonth()), FormFragment.DATE_PATTERN)
    }

    private fun setTariff(tariff: EnergyTariff) {
        tariffLiveData.value = tariff
    }
}
