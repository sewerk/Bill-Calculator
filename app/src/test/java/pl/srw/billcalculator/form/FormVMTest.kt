package pl.srw.billcalculator.form

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.view.View
import com.nhaarman.mockito_kotlin.*
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.wrapper.PricesRepo

@RunWith(JUnitParamsRunner::class)
class FormVMTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val provider = Provider.PGE
    val tariffLiveData = MutableLiveData<String>()
    val pricesRepo: PricesRepo = mock {
        on { tariffPge } doReturn tariffLiveData
        on { tariffTauron } doReturn tariffLiveData
    }
    var sut = FormVM(provider, pricesRepo)

    @Test
    fun `initialize fromDate with first day of current month`() {
        assert(calculateDateFrom() == sut.dateFrom.get())
    }

    @Test
    fun `initialize toDate with last dat of current month`() {
        assert(calculateDateTo() == sut.dateTo.get())
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
    @Parameters("PGE", "TAURON")
    fun `single readings are visible when tariff G11`(provider: Provider) {
        sut = FormVM(provider, pricesRepo)
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G11)

        assert(View.VISIBLE == sut.singleReadingsVisibility)
        assert(View.GONE == sut.doubleReadingsVisibility)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun `double readings are visible when tariff G12`(provider: Provider) {
        sut = FormVM(provider, pricesRepo)
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        assert(View.GONE == sut.singleReadingsVisibility)
        assert(View.VISIBLE == sut.doubleReadingsVisibility)
    }

    @Test
    fun `single readings are visible when PGNIG provider`() {
        sut = FormVM(Provider.PGNIG, pricesRepo)

        assert(View.VISIBLE == sut.singleReadingsVisibility)
        assert(View.GONE == sut.doubleReadingsVisibility)
    }

    @Test
    fun `opens settings screen for provider when settings linked clicked`() {
        val observer:Observer<Provider?> = mock()
        sut.openSettingsCommand.observeForever(observer)

        sut.settingsLinkClicked()

        verify(observer).onChanged(provider)
    }

    private fun calculateDateFrom() = Dates.format(LocalDate.now().withDayOfMonth(1), FormFragment.DATE_PATTERN)

    private fun calculateDateTo(): String {
        val now = LocalDate.now()
        return Dates.format(now.withDayOfMonth(now.lengthOfMonth()), FormFragment.DATE_PATTERN)
    }

    private fun setTariff(@SharedPreferencesEnergyPrices.TariffOption tariff: String) {
        tariffLiveData.value = tariff
        whenever(pricesRepo.getTariff(any())).thenReturn(tariff)
    }
}