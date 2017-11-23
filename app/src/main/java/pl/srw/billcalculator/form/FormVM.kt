package pl.srw.billcalculator.form

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.wrapper.PricesRepo
import pl.srw.billcalculator.wrapper.ReadingsRepo
import javax.inject.Inject

class FormVM @Inject constructor(private val readingsRepo: ReadingsRepo,
                                 private val pricesRepo: PricesRepo) : ViewModel() {

    var fromDate = Dates.firstDayOfThisMonth()
    var toDate = Dates.lastDayOfThisMonth()

    var provider: Provider? = null
        private set
    lateinit var singlePrevReadings: LiveData<IntArray>
        private set
    lateinit var dayPrevReadings: LiveData<IntArray>
        private set
    lateinit var nightPrevReadings: LiveData<IntArray>
        private set

    private val emptyLiveData = MutableLiveData<IntArray>()
    private val pgeTariffObserver = Observer<String> { t ->
        if (SharedPreferencesEnergyPrices.TARIFF_G11 == t) fetchSinglePrevReadingsFor(Provider.PGE)
        else fetchDoublePrevReadingsFor(Provider.PGE)
    }
    private val tauronTariffObserver = Observer<String> { t ->
        if (SharedPreferencesEnergyPrices.TARIFF_G11 == t) fetchSinglePrevReadingsFor(Provider.TAURON)
        else fetchDoublePrevReadingsFor(Provider.TAURON)
    }

    init {
        emptyLiveData.value = intArrayOf()
        pricesRepo.tariffPge.observeForever(pgeTariffObserver)
        pricesRepo.tariffTauron.observeForever(tauronTariffObserver)
    }

    fun init(provider: Provider) {
        if (this.provider != null) return
        this.provider = provider
        loadPrevReadings(provider)
    }

    override fun onCleared() {
        super.onCleared()
        pricesRepo.tariffTauron.removeObserver(tauronTariffObserver)
        pricesRepo.tariffPge.removeObserver(pgeTariffObserver)
    }

    private fun loadPrevReadings(provider: Provider) {
        if (provider == Provider.PGNIG
                || SharedPreferencesEnergyPrices.TARIFF_G11 == pricesRepo.getTariff(provider)) {
            fetchSinglePrevReadingsFor(provider)
        } else fetchDoublePrevReadingsFor(provider)
    }

    private fun fetchDoublePrevReadingsFor(provider: Provider) {
        singlePrevReadings = emptyLiveData
        provider.doubleReadingTypes[0]?.let { dayPrevReadings = readingsRepo.getPreviousReadingsFor(it) }
        provider.doubleReadingTypes[1]?.let { nightPrevReadings = readingsRepo.getPreviousReadingsFor(it) }
    }

    private fun fetchSinglePrevReadingsFor(provider: Provider) {
        singlePrevReadings = readingsRepo.getPreviousReadingsFor(provider.singleReadingType)
        dayPrevReadings = emptyLiveData
        nightPrevReadings = emptyLiveData
    }

}