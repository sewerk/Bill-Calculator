package pl.srw.billcalculator.history.list

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pl.srw.billcalculator.data.ApplicationRepo
import pl.srw.billcalculator.data.bill.HistoryRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryVMFactory @Inject constructor(
    private val applicationRepo: ApplicationRepo,
    private val historyRepo: HistoryRepo
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(HistoryVM::class.java) -> HistoryVM(applicationRepo, historyRepo)
                else -> throw IllegalArgumentException("Don't know how to create ${modelClass.simpleName}")
            }
        } as T

}
