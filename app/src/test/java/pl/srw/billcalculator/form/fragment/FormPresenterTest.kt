package pl.srw.billcalculator.form.fragment

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import pl.srw.billcalculator.RxJavaBaseTest
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.wrapper.PricesRepo

@RunWith(JUnitParamsRunner::class)
class FormPresenterTest : RxJavaBaseTest() {

    val view: FormPresenter.FormView = mock()
    val prices: SharedPreferencesEnergyPrices = mock()
    val pricesRepo: PricesRepo = mock {
        on { getTariff(any())} doReturn SharedPreferencesEnergyPrices.TARIFF_G11
    }
    val historyUpdater: FormPresenter.HistoryChangeListener = mock()

    var sut = FormPresenter(view, Provider.PGE, pricesRepo, historyUpdater)

    @Test
    fun whenCloseButtonClicked_dismissForm() {
        sut.closeButtonClicked()

        verify(view).hideForm()
    }

    @Test
    @Parameters("PGE", "PGNIG", "TAURON")
    fun calculateButtonClicked_forTariffG11_whenValuesCorrect_saveAndOpenBillForSingleReadings(provider: Provider) {
        setup(provider)
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.12.16", "11", "12", "22", "23")

        verify(view).startStoringServiceForSingleReadings(provider)
        verify(view).startBillActivityForSingleReadings(provider)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun calculateButtonClicked_forEnergyProvider_andTariffG12_whenValuesCorrect_saveAndOpenBillForDoubleReadings(provider: Provider) {
        setup(provider)
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.12.16", "11", "12", "22", "23")

        verify(view).startStoringServiceForDoubleReadings(provider)
        verify(view).startBillActivityForDoubleReadings(provider)
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_whenValuesCorrect_dismissForm(provider: Provider, tariff: String) {
        setup(provider)
        given_tariff(tariff)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.12.16", "11", "12", "22", "23")

        verify(view).hideForm()
    }

    @Test
    fun calculateButtonClicked_cleansErrorsOnFields() {
        sut.calculateButtonClicked("", "", "", "", "", "", "", "")

        verify(view).cleanErrorsOnFields()
    }

    @Test
    fun calculateButtonClicked_forPGNIG_whenReadingValueIncorrect_showsReadingsError() {
        setup(Provider.PGNIG)

        sut.calculateButtonClicked("", "", "28.12.16", "31.12.16", "", "", "", "")

        verify(view).showReadingFieldError(any(), anyInt())
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_forEnergyProvider_whenReadingValueIncorrect_showsReadingsError(provider: Provider, tariff: String) {
        setup(provider)
        given_tariff(tariff)

        sut.calculateButtonClicked("", "", "28.12.16", "31.12.16", "", "", "", "")

        verify(view).showReadingFieldError(any(), anyInt())
    }

    @Suppress("unused")
    private fun paramsEnergyProviderWithTariff() = arrayOf(
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11),
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12)
    )

    @Test
    fun calculateButtonClicked_forPGNIG_whenDateValueIncorrect_showsDateError() {
        setup(Provider.PGNIG)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.11.16", "11", "12", "22", "23")

        verify(view).showDateFieldError(anyInt())
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_forEnergyProvider_whenDateValueIncorrect_showsDateError(provider: Provider, tariff: String) {
        setup(provider)
        given_tariff(tariff)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.11.16", "11", "12", "22", "23")

        verify(view).showDateFieldError(anyInt())
    }

    @Test
    fun `refetch history list when calculate button clicked for single readings and validation passed`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)

        sut.calculateButtonClicked("1", "2", "28.12.16" , "31.12.16", "", "", "", "")

        verify(historyUpdater).onHistoryChanged()
    }

    @Test
    fun `refetch history list when calculate button clicked for double readings and validation passed`() {
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        sut.calculateButtonClicked("", "", "28.12.16" , "31.12.16", "11", "12", "21", "22")

        verify(historyUpdater).onHistoryChanged()
    }

    private fun given_tariff(@SharedPreferencesEnergyPrices.TariffOption tariff: String) {
        whenever(pricesRepo.getTariff(any())).thenReturn(tariff)
    }

    private fun setup(provider: Provider) {
        sut = FormPresenter(view, provider, pricesRepo, historyUpdater)
    }
}
