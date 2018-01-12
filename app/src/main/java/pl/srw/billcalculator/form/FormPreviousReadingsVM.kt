package pl.srw.billcalculator.form

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pl.srw.billcalculator.data.bill.ReadingsRepo
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.plusAssign
import timber.log.Timber

class FormPreviousReadingsVM(private val provider: Provider,
                             private val readingsRepo: ReadingsRepo,
                             private val pricesRepo: PricesRepo) : ViewModel() {

    val singlePrevReadings = MutableLiveData<IntArray>()
    val dayPrevReadings = MutableLiveData<IntArray>()
    val nightPrevReadings = MutableLiveData<IntArray>()

    private val subscriptions = CompositeDisposable()
    private val pgeTariffObserver = Observer<String> { t ->
        if (SharedPreferencesEnergyPrices.TARIFF_G11 == t) fetchSinglePrevReadingsFor(Provider.PGE)
        else fetchDoublePrevReadingsFor(Provider.PGE)
    }
    private val tauronTariffObserver = Observer<String> { t ->
        if (SharedPreferencesEnergyPrices.TARIFF_G11 == t) fetchSinglePrevReadingsFor(Provider.TAURON)
        else fetchDoublePrevReadingsFor(Provider.TAURON)
    }

    init {
        when (provider) {
            Provider.PGE -> pricesRepo.tariffPge.observeForever(pgeTariffObserver)
            Provider.TAURON -> pricesRepo.tariffTauron.observeForever(tauronTariffObserver)
            else -> fetchSinglePrevReadingsFor(Provider.PGNIG)
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
        if (provider == Provider.PGE) pricesRepo.tariffPge.removeObserver(pgeTariffObserver)
        else if (provider == Provider.TAURON) pricesRepo.tariffTauron.removeObserver(tauronTariffObserver)
    }

    private fun fetchSinglePrevReadingsFor(provider: Provider) {
        subscriptions += readingsRepo.getPreviousReadingsFor(provider.singleReadingType)
                .subscribeOn(Schedulers.io())
                .subscribe(singlePrevReadings::postValue, logException(provider))
    }

    private fun fetchDoublePrevReadingsFor(provider: Provider) {
        provider.doubleReadingTypes[0]?.let { subscriptions += readingsRepo.getPreviousReadingsFor(it)
                .subscribeOn(Schedulers.io())
                .subscribe(dayPrevReadings::postValue, logException(provider))
        }
        provider.doubleReadingTypes[1]?.let { subscriptions += readingsRepo.getPreviousReadingsFor(it)
                .subscribeOn(Schedulers.io())
                .subscribe(nightPrevReadings::postValue, logException(provider))
        }
    }

    private fun logException(provider: Provider): (Throwable) -> Unit =
            { Timber.e(it, "Error retrieving previous readings for $provider") }
}
