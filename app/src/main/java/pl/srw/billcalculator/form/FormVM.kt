package pl.srw.billcalculator.form

import android.arch.lifecycle.Observer
import android.databinding.Bindable
import android.os.Bundle
import android.view.View
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import pl.srw.billcalculator.BR
import pl.srw.billcalculator.common.binding.notifiable.ObservableViewModel
import pl.srw.billcalculator.common.binding.notifiable.bindable
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.util.SingleLiveEvent
import pl.srw.billcalculator.util.StateRestorable
import timber.log.Timber

private const val DEFAULT_TARIFF_LABEL_FOR_PGNIG = ""

class FormVM(val provider: Provider,
             private val pricesRepo: PricesRepo)
    : ObservableViewModel(), StateRestorable {

    // static properties
    val logoResource = provider.logoRes
    val readingsUnitTextResource = provider.formReadingUnit

    // read-only properties
    var tariffLabel: String by bindable(DEFAULT_TARIFF_LABEL_FOR_PGNIG, BR.tariffLabel)
        @Bindable get
        private set
    var singleReadingsVisibility: Int by bindable(View.VISIBLE, BR.singleReadingsVisibility)
        @Bindable get
        private set
    var doubleReadingsVisibility: Int by bindable(View.GONE, BR.doubleReadingsVisibility)
        @Bindable get
        private set

    // read-write properties
    @get:Bindable var readingFrom: String by bindable("", BR.readingFrom)
    @get:Bindable var readingTo: String by bindable("", BR.readingTo)
    @get:Bindable var readingDayFrom: String by bindable("", BR.readingDayFrom)
    @get:Bindable var readingDayTo: String by bindable("", BR.readingDayTo)
    @get:Bindable var readingNightFrom: String by bindable("", BR.readingNightFrom)
    @get:Bindable var readingNightTo: String by bindable("", BR.readingNightTo)
    @get:Bindable var dateFrom: String by bindable(Dates.firstDayOfThisMonth().toText(), BR.dateFrom)
    @get:Bindable var dateTo: String by bindable(Dates.lastDayOfThisMonth().toText(), BR.dateTo)

    // commands
    val openSettingsCommand = SingleLiveEvent<Provider>()

    private val tariffObserver = Observer<EnergyTariff> {
        val tariff = it!!
        tariffLabel = tariff.name
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

    override fun writeTo(bundle: Bundle) {
        FormStateHelper.put(bundle, this)
    }

    override fun readFrom(bundle: Bundle) {
        FormStateHelper.retrieve(bundle, this)
    }

    fun settingsLinkClicked() {
        Timber.i("Form: Settings link clicked")
        openSettingsCommand.value = provider
    }

    fun isSingleReadingsProcessing() = singleReadingsVisibility == View.VISIBLE

    private fun setReadingsVisibility(tariff: EnergyTariff) {
        if (tariff == EnergyTariff.G11) {
            singleReadingsVisibility = View.VISIBLE
            doubleReadingsVisibility = View.GONE
        } else {
            singleReadingsVisibility = View.GONE
            doubleReadingsVisibility = View.VISIBLE
        }
    }

    private fun LocalDate.toText(): String = format(DateTimeFormatter.ofPattern(FormFragment.DATE_PATTERN))
}
