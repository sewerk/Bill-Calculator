package pl.srw.billcalculator.form.fragment

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import pl.srw.billcalculator.RxJavaBaseTest
import pl.srw.billcalculator.form.FormVM
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices.TARIFF_G11
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices.TARIFF_G12
import pl.srw.billcalculator.type.Provider

@RunWith(JUnitParamsRunner::class)
class FormPresenterTest : RxJavaBaseTest() {

    val view: FormPresenter.FormView = mock()
    val historyUpdater: FormPresenter.HistoryChangeListener = mock()

    var sut = FormPresenter(view, Provider.PGE, historyUpdater)

    @Test
    fun whenCloseButtonClicked_dismissForm() {
        sut.closeButtonClicked()

        verify(view).hideForm()
    }

    @Test
    @Parameters("PGE", "PGNIG", "TAURON")
    fun calculateButtonClicked_forTariffG11_whenValuesCorrect_saveAndOpenBillForSingleReadings(provider: Provider) {
        setup(provider)
        val vm = mockFormVM(rf = "1", rt = "2", df = "28.12.16", dt = "31.12.16",
                rdf = "11", rdt = "12", rnf = "22", rnt = "23", tariff = TARIFF_G11)

        sut.calculateButtonClicked(vm)

        verify(view).startStoringService(provider)
        verify(view).startBillActivity(provider)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun calculateButtonClicked_forEnergyProvider_andTariffG12_whenValuesCorrect_saveAndOpenBillForDoubleReadings(provider: Provider) {
        setup(provider)
        val vm = mockFormVM(rf = "1", rt = "2", df = "28.12.16", dt = "31.12.16",
                rdf = "11", rdt = "12", rnf = "22", rnt = "23", tariff = TARIFF_G12)

        sut.calculateButtonClicked(vm)

        verify(view).startStoringService(provider)
        verify(view).startBillActivity(provider)
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_whenValuesCorrect_dismissForm(provider: Provider, tariff: String) {
        setup(provider)
        val vm = mockFormVM(rf = "1", rt = "2", df = "28.12.16", dt = "31.12.16",
                rdf = "11", rdt = "12", rnf = "22", rnt = "23", tariff = tariff)

        sut.calculateButtonClicked(vm)

        verify(view).hideForm()
    }

    @Test
    fun calculateButtonClicked_cleansErrorsOnFields() {
        sut.calculateButtonClicked(mockFormVM())

        verify(view).cleanErrorsOnFields()
    }

    @Test
    fun calculateButtonClicked_forPGNIG_whenReadingValueIncorrect_showsReadingsError() {
        setup(Provider.PGNIG)
        val vm = mockFormVM(df = "28.12.16", dt = "31.12.16")

        sut.calculateButtonClicked(vm)

        verify(view).showReadingFieldError(any(), anyInt())
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_forEnergyProvider_whenReadingValueIncorrect_showsReadingsError(provider: Provider, tariff: String) {
        setup(provider)
        val vm = mockFormVM(df = "28.12.16", dt = "31.12.16", tariff = tariff)

        sut.calculateButtonClicked(vm)

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
        val vm = mockFormVM(rf = "1", rt = "2", df = "28.12.16", dt = "31.11.16", rdf = "11", rdt = "12", rnf = "22", rnt = "23")

        sut.calculateButtonClicked(vm)

        verify(view).showDateFieldError(anyInt())
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_forEnergyProvider_whenDateValueIncorrect_showsDateError(provider: Provider, tariff: String) {
        setup(provider)
        val vm = mockFormVM(rf = "1", rt = "2", df = "28.12.16", dt = "31.11.16",
                rdf = "11", rdt = "12", rnf = "22", rnt = "23", tariff = tariff)

        sut.calculateButtonClicked(vm)

        verify(view).showDateFieldError(anyInt())
    }

    @Test
    fun `refetch history list when calculate button clicked for single readings and validation passed`() {
        val vm = mockFormVM(rf = "1", rt = "2", df = "28.12.16", dt = "31.12.16", tariff = TARIFF_G11)

        sut.calculateButtonClicked(vm)

        verify(historyUpdater).onHistoryChanged()
    }

    @Test
    fun `refetch history list when calculate button clicked for double readings and validation passed`() {
        val vm = mockFormVM(rdf = "11", rdt = "12", rnf = "21", rnt = "22",
                df = "28.12.16", dt = "31.12.16", tariff = TARIFF_G12)

        sut.calculateButtonClicked(vm)

        verify(historyUpdater).onHistoryChanged()
    }

    private fun setup(provider: Provider) {
        sut = FormPresenter(view, provider, historyUpdater)
    }

    private fun mockFormVM(rf: String = "", rt: String = "",
                           df:String = "", dt:String = "",
                           rdf: String = "", rdt: String = "",
                           rnf: String="", rnt: String = "",
                           tariff: String = TARIFF_G11): FormVM {
        return mock {
            on { readingFrom } doReturn rf
            on { readingTo } doReturn rt
            on { readingDayFrom } doReturn rdf
            on { readingDayTo } doReturn rdt
            on { readingNightFrom } doReturn rnf
            on { readingNightTo } doReturn rnt
            on { dateFrom } doReturn df
            on { dateTo } doReturn dt
            on { tariffLabel } doReturn tariff
            on { isSingleReadingsProcessing() } doReturn(tariff == TARIFF_G11)
        }
    }
}
