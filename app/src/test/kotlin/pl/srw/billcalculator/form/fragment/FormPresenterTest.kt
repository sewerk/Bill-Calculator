package pl.srw.billcalculator.form.fragment

import android.support.annotation.StringRes
import android.view.View
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.verification.VerificationMode
import org.threeten.bp.LocalDate
import pl.srw.billcalculator.R
import pl.srw.billcalculator.RxJavaBaseTest
import pl.srw.billcalculator.setState
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper
import pl.srw.billcalculator.wrapper.ReadingsRepo

@RunWith(JUnitParamsRunner::class)
class FormPresenterTest : RxJavaBaseTest() {

    val view: FormPresenter.FormView = mock()
    val prices: SharedPreferencesEnergyPrices = mock()
    val providerMapper: ProviderMapper = mock {
        on { getPrices(anyOrNull())} doReturn prices
    }
    val readingsRepo: ReadingsRepo = mock {
        on { getPreviousReadingsFor(any())} doReturn Single.never<IntArray>()
    }
    val historyUpdater: FormPresenter.HistoryUpdating = mock()

    val sut = FormPresenter(readingsRepo, providerMapper, historyUpdater)

    @Before
    fun setUp() {
        sut.setState("view", view)
    }

    @Test
    @Parameters(method = "paramsForSetFormValues")
    fun onFirstBind_setFormValues(provider: Provider, tariff: String?, @StringRes readingUnitResId: Int) {
        // GIVEN
        val dateFrom = calculateDateFrom()
        val dateTo = calculateDateTo()
        sut.setup(provider)
        if (tariff != null) given_tariff(tariff)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view).setLogo(provider)
        verify(view).setupSettingsLink()
        verify(view, times(if (tariff != null) 1 else 0)).setTariffText(tariff)
        verify(view).setReadingUnit(readingUnitResId)
        verify(view).setDates(dateFrom, dateTo)
    }

    private fun paramsForSetFormValues() = arrayOf(
                arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11, R.string.form_reading_unit_kWh),
                arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12, R.string.form_reading_unit_kWh),
                arrayOf(Provider.PGNIG, null, R.string.form_reading_unit_m3),
                arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11, R.string.form_reading_unit_kWh),
                arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12, R.string.form_reading_unit_kWh)
    )

    @Test
    @Parameters(method = "paramsForSetFormValues")
    fun onNewViewRestoreState_setFormValues(provider: Provider, tariff: String?, @StringRes readingUnitResId: Int) {
        // GIVEN
        sut.setup(provider)
        if (tariff != null) given_tariff(tariff)

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(view).setLogo(provider)
        verify(view).setupSettingsLink()
        verify(view, times(if (tariff != null) 1 else 0)).setTariffText(tariff)
        verify(view).setReadingUnit(readingUnitResId)
        verify(view, never()).setDates(any(), any())
    }

    @Test
    @Parameters(value = *arrayOf("PGE", "PGNIG", "TAURON"))
    fun whenSettingsLinkClicked_showProviderSettings(provider: Provider) {
        sut.setup(provider)

        sut.settingsLinkClicked()

        verify(view).showProviderSettings(provider)
    }

    @Test
    fun whenCloseButtonClicked_dismissForm() {
        sut.closeButtonClicked()

        verify(view).hideForm()
    }

    @Test
    fun onFirstBind_forPGNIG_hidesDoubleReadingsVisibility() {
        sut.setup(Provider.PGNIG)

        sut.onFirstBind()

        verify(view).setDoubleReadingsVisibility(View.GONE)
    }

    @Test
    fun onNewViewRestoreState_forPGNIG_hidesDoubleReadingsVisibility() {
        sut.setup(Provider.PGNIG)

        sut.onNewViewRestoreState()

        verify(view).setDoubleReadingsVisibility(View.GONE)
    }

    @Test
    @Parameters(method = "paramsForReadingVisibility")
    fun onFirstBind_hidesReadingsVisibility(provider: Provider, tariff: String, singleVisibility: Int, doubleVisibility: Int) {
        sut.setup(provider)
        given_tariff(tariff)

        sut.onFirstBind()

        verify(view).setSingleReadingsVisibility(singleVisibility)
        verify(view).setDoubleReadingsVisibility(doubleVisibility)
    }

    private fun paramsForReadingVisibility() = arrayOf(
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11, View.VISIBLE, View.GONE),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11, View.VISIBLE, View.GONE),
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12, View.GONE, View.VISIBLE),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12, View.GONE, View.VISIBLE)
    )

    @Test
    @Parameters(method = "paramsForReadingVisibility")
    fun onNewViewRestoreState_hidesReadingsVisibility(provider: Provider, tariff: String, singleVisibility: Int, doubleVisibility: Int) {
        sut.setup(provider)
        given_tariff(tariff)

        sut.onNewViewRestoreState()

        verify(view).setSingleReadingsVisibility(singleVisibility)
        verify(view).setDoubleReadingsVisibility(doubleVisibility)
    }

    @Test
    @Parameters(value = *arrayOf("PGE", "PGNIG", "TAURON"))
    fun calculateButtonClicked_forTariffG11_whenValuesCorrect_saveAndOpenBillForSingleReadings(
            provider: Provider) {
        sut.setup(provider)
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G11)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.12.16", "11", "12", "22", "23")

        verify(view).startStoringServiceForSingleReadings(provider)
        verify(view).startBillActivityForSingleReadings(provider)
    }

    @Test
    @Parameters(value = *arrayOf("PGE", "TAURON"))
    fun calculateButtonClicked_forEnergyProvider_andTariffG12_whenValuesCorrect_saveAndOpenBillForDoubleReadings(
            provider: Provider) {
        sut.setup(provider)
        given_tariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.12.16", "11", "12", "22", "23")

        verify(view).startStoringServiceForDoubleReadings(provider)
        verify(view).startBillActivityForDoubleReadings(provider)
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_whenValuesCorrect_dismissForm(provider: Provider, tariff: String) {
        sut.setup(provider)
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
        sut.setup(Provider.PGNIG)

        sut.calculateButtonClicked("", "", "28.12.16", "31.12.16", "", "", "", "")

        verify(view).showReadingFieldError(any(), anyInt())
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_forEnergyProvider_whenReadingValueIncorrect_showsReadingsError(provider: Provider, tariff: String) {
        sut.setup(provider)
        given_tariff(tariff)

        sut.calculateButtonClicked("", "", "28.12.16", "31.12.16", "", "", "", "")

        verify(view).showReadingFieldError(any(), anyInt())
    }

    private fun paramsEnergyProviderWithTariff() = arrayOf(
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11),
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12)
    )

    @Test
    fun calculateButtonClicked_forPGNIG_whenDateValueIncorrect_showsDateError() {
        sut.setup(Provider.PGNIG)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.11.16", "11", "12", "22", "23")

        verify(view).showDateFieldError(anyInt())
    }

    @Test
    @Parameters(method = "paramsEnergyProviderWithTariff")
    fun calculateButtonClicked_forEnergyProvider_whenDateValueIncorrect_showsDateError(provider: Provider, tariff: String) {
        sut.setup(provider)
        given_tariff(tariff)

        sut.calculateButtonClicked("1", "2", "28.12.16", "31.11.16", "11", "12", "22", "23")

        verify(view).showDateFieldError(anyInt())
    }

    @Test
    @Parameters(method = "paramsForAutoComplete")
    fun onFirstBind_setsAutoCompletionDataForReadingFrom(
            provider: Provider, tariff: String, singleVerificationMode: VerificationMode, doubleVerificationMode: VerificationMode) {
        val readings = intArrayOf(1, 2, 3)
        given_databaseReturnPreviousReadings(readings)
        sut.setup(provider)
        given_tariff(tariff)

        sut.onFirstBind()
        waitToFinish()

        verify(view, singleVerificationMode).setAutoCompleteDataForReadingFrom(readings)
        verify(view, doubleVerificationMode).setAutoCompleteDataForReadingDayFrom(readings)
        verify(view, doubleVerificationMode).setAutoCompleteDataForReadingNightFrom(readings)
    }

    private fun paramsForAutoComplete() = arrayOf(
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11, times(1), never()),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11, times(1), never()),
            arrayOf(Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12, never(), times(1)),
            arrayOf(Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12, never(), times(1))
    )

    @Test
    @Parameters(method = "paramsForAutoComplete")
    fun onNewViewRestoreState_setsAutoCompletionDataForReadingFrom(
            provider: Provider, tariff: String, singleVerificationMode: VerificationMode, doubleVerificationMode: VerificationMode) {
        val readings = intArrayOf(1, 2, 3)
        given_databaseReturnPreviousReadings(readings)
        sut.setup(provider)
        given_tariff(tariff)

        sut.onNewViewRestoreState()
        waitToFinish()

        verify(view, singleVerificationMode).setAutoCompleteDataForReadingFrom(readings)
        verify(view, doubleVerificationMode).setAutoCompleteDataForReadingDayFrom(readings)
        verify(view, doubleVerificationMode).setAutoCompleteDataForReadingNightFrom(readings)
    }

    @Test
    fun onFirstBind_forPGNIG_setsAutoCompletionDataForReadingFrom() {
        val readings = intArrayOf(1, 2, 3)
        given_databaseReturnPreviousReadings(readings)
        sut.setup(Provider.PGNIG)

        sut.onFirstBind()
        waitToFinish()

        verify(view).setAutoCompleteDataForReadingFrom(readings)
    }

    @Test
    fun onNewViewRestoreState_forPGNIG_setsAutoCompletionDataForReadingFrom() {
        val readings = intArrayOf(1, 2, 3)
        given_databaseReturnPreviousReadings(readings)
        sut.setup(Provider.PGNIG)

        sut.onNewViewRestoreState()
        waitToFinish()

        verify(view).setAutoCompleteDataForReadingFrom(readings)
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
        whenever(prices.tariff).thenReturn(tariff)
    }

    private fun given_databaseReturnPreviousReadings(readings: IntArray) {
        whenever(readingsRepo.getPreviousReadingsFor(any())).thenReturn(Single.just(readings))
    }

    private fun calculateDateFrom(): LocalDate {
        return LocalDate.now().withDayOfMonth(1)
    }

    private fun calculateDateTo(): LocalDate {
        val now = LocalDate.now()
        return now.withDayOfMonth(now.lengthOfMonth())
    }
}