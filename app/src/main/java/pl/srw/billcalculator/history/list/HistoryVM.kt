package pl.srw.billcalculator.history.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import pl.srw.billcalculator.data.ApplicationRepo
import pl.srw.billcalculator.data.bill.HistoryRepo
import pl.srw.billcalculator.db.History
import timber.log.Timber

class HistoryVM(
    private val applicationRepo: ApplicationRepo,
    private val historyRepo: HistoryRepo
) : ViewModel() {

    val data = MutableLiveData<List<History>>()

    private val historyObserver = Observer<List<History>> {
        updateHistoryResult(it!!)
    }

    init {
        historyRepo.getAll().observeForever(historyObserver)
    }

    override fun onCleared() {
        super.onCleared()
        historyRepo.getAll().removeObserver(historyObserver)
    }

    private fun updateHistoryResult(history: List<History>) {
        Timber.i("history size = %d", history.size)
        data.value = history

        if (applicationRepo.isFirstLaunch && history.isNotEmpty()) {
            Timber.e(IllegalStateException("Db exist with clean SharedPrefs"))
        }
    }
}
