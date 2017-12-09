package pl.srw.billcalculator.form

import android.arch.lifecycle.Observer
import android.databinding.Bindable
import android.databinding.ObservableField
import android.view.View
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import pl.srw.billcalculator.BR
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.util.SingleLiveEvent
import pl.srw.billcalculator.util.binding.notifiable.ObservableViewModel
import pl.srw.billcalculator.util.binding.notifiable.bindable
import pl.srw.billcalculator.wrapper.Analytics
import pl.srw.billcalculator.wrapper.PricesRepo

private const val DEFAULT_TARIFF_LABEL_FOR_PGNIG = ""

class FormVM(val provider: Provider,
             private val pricesRepo: PricesRepo)
    : ObservableViewModel() {

    // static properties
    val logoResource = provider.logoRes
    val readingsUnitTextResource = provider.formReadingUnit

    // read-only properties
    @get:Bindable
    var tariffLabel: String by bindable(DEFAULT_TARIFF_LABEL_FOR_PGNIG, BR.tariffLabel)
        private set

    @get:Bindable
    var singleReadingsVisibility: Int by bindable(View.VISIBLE, BR.singleReadingsVisibility)
        private set

    @get:Bindable
    var doubleReadingsVisibility: Int by bindable(View.GONE, BR.doubleReadingsVisibility)
        private set

    // read-write properties
    val readingFrom = ObservableField("")
    val readingTo = ObservableField("")
    val readingDayFrom = ObservableField("")
    val readingDayTo = ObservableField("")
    val readingNightFrom = ObservableField("")
    val readingNightTo = ObservableField("")

    val dateFrom = ObservableField(Dates.firstDayOfThisMonth().toText())
    val dateTo = ObservableField(Dates.lastDayOfThisMonth().toText())

    // commands
    val openSettingsCommand = SingleLiveEvent<Provider>()

    private val tariffObserver = Observer<String> {
        val tariff = it!!
        tariffLabel = tariff
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
            singleReadingsVisibility = View.VISIBLE
            doubleReadingsVisibility = View.GONE
        } else {
            singleReadingsVisibility = View.GONE
            doubleReadingsVisibility = View.VISIBLE
        }
    }

    private fun LocalDate.toText(): String = format(DateTimeFormatter.ofPattern(FormFragment.DATE_PATTERN))
}