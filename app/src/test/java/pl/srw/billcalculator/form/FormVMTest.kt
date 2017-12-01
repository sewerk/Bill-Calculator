@file:Suppress("IllegalIdentifier")
package pl.srw.billcalculator.form

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.wrapper.PricesRepo

@Suppress("MemberVisibilityCanPrivate")
@RunWith(JUnitParamsRunner::class)
class FormVMTest {

    @Rule @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    val provider = Provider.PGE
    val tariffLiveData = MutableLiveData<String>()
    val pricesRepo: PricesRepo = mock {
        on { tariffPge } doReturn tariffLiveData
        on { tariffTauron } doReturn tariffLiveData
    }
    var sut = FormVM(provider, pricesRepo)

    @Test
    fun `initialize fromDate with first day of current month`() {
        assert(calculateDateFrom() == sut.fromDate)
    }

    @Test
    fun `initialize toDate with last dat of current month`() {
        assert(calculateDateTo() == sut.toDate)
    }

    @Test
    fun `initialize provider logo with given provider`() {
        assert(provider.logoRes == sut.logoResource)
    }

    @Test
    fun `sets readings unit for given provider`() {
        assert(provider.formReadingUnit == sut.readingsUnitTextResource)
    }

    @Test
    @Parameters(SharedPreferencesEnergyPrices.TARIFF_G11, SharedPreferencesEnergyPrices.TARIFF_G12)
    fun `sets tariff label for energy providers`(tariff: String) {
        setTariff(tariff)

        assert(tariff == sut.tariffLabel)
    }

    @Test
    fun `sets empty tariff label for PGNIG`() {
        sut = FormVM(Provider.PGNIG, pricesRepo)

        assert("" == sut.tariffLabel)
    }

    @Test
    fun `opens settings screen for provider when settings linked clicked`() {
        val observer:Observer<Provider?> = mock()
        sut.openSettingsCommand.observeForever(observer)

        sut.settingsLinkClicked()

        verify(observer).onChanged(provider)
    }

    private fun calculateDateFrom(): LocalDate = LocalDate.now().withDayOfMonth(1)

    private fun calculateDateTo(): LocalDate {
        val now = LocalDate.now()
        return now.withDayOfMonth(now.lengthOfMonth())
    }

    private fun setTariff(@SharedPreferencesEnergyPrices.TariffOption tariff: String) {
        tariffLiveData.value = tariff
        whenever(pricesRepo.getTariff(any())).thenReturn(tariff)
    }
}