package pl.srw.billcalculator.form

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.util.SingleLiveEvent
import pl.srw.billcalculator.wrapper.Analytics
import pl.srw.billcalculator.wrapper.PricesRepo

const val DEFAULT_TARIFF_LABEL_FOR_PGNIG = ""

class FormVM(val provider: Provider,
             private val pricesRepo: PricesRepo) : ViewModel() {

    val logoResource = provider.logoRes
    val tariffLabel = ObservableField<String>(DEFAULT_TARIFF_LABEL_FOR_PGNIG)
    val readingsUnitTextResource = provider.formReadingUnit
    val singleReadingsVisibility = ObservableInt(View.VISIBLE)
    val doubleReadingsVisibility = ObservableInt(View.GONE)
    var fromDate = ObservableField(Dates.firstDayOfThisMonth().toText())
    var toDate = ObservableField(Dates.lastDayOfThisMonth().toText())
    val openSettingsCommand = SingleLiveEvent<Provider>()

    private val tariffObserver = Observer<String> {
        val tariff = it!!
        tariffLabel.set(tariff)
        setReadingsVisibility(tariff)
    }

    init {
        if (provider == Provider.PGE) pricesRepo.tariffPge.observeForever(tariffObserver)
        else if (provider == Provider.TAURON) pricesRepo.tariffTauron.observeForever(tariffObserver)
    }

    override fun onCleared() {
        super.onCleared()
        if (provider == Provider.PGE) pricesRepo.tariffPge.removeObserver(tariffObserver)
        else if (provider == Provider.TAURON) pricesRepo.tariffTauron.removeObserver(tariffObserver)
    }

    fun settingsLinkClicked() {
        Analytics.log("Form: Settings link clicked")
        openSettingsCommand.value = provider
    }

    private fun setReadingsVisibility(tariff: String) {
        if (tariff == SharedPreferencesEnergyPrices.TARIFF_G11) {
            singleReadingsVisibility.set(View.VISIBLE)
            doubleReadingsVisibility.set(View.GONE)
        } else {
            singleReadingsVisibility.set(View.GONE)
            doubleReadingsVisibility.set(View.VISIBLE)
        }
    }

    private fun LocalDate.toText(): String = format(DateTimeFormatter.ofPattern(FormFragment.DATE_PATTERN))
}