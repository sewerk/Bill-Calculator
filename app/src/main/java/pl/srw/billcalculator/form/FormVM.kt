package pl.srw.billcalculator.form

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.util.SingleLiveEvent
import pl.srw.billcalculator.wrapper.PricesRepo

const val DEFAULT_TARIFF_LABEL_FOR_PGNIG = ""

class FormVM(val provider: Provider,
             private val pricesRepo: PricesRepo) : ViewModel() {

    var fromDate = Dates.firstDayOfThisMonth()
    var toDate = Dates.lastDayOfThisMonth()
    val logoResource = provider.logoRes
    var tariffLabel = ObservableField<String>(DEFAULT_TARIFF_LABEL_FOR_PGNIG)
    val readingsUnitTextResource = provider.formReadingUnit
    val openSettingsCommand = SingleLiveEvent<Provider>()

    private val tariffObserver = Observer<String> { tariffLabel.set(it!!) }

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
        openSettingsCommand.value = provider
    }
}