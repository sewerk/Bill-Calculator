package pl.srw.billcalculator.settings.details

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDetailsVMFactory @Inject constructor(private val pricesRepo: PricesRepo) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SettingsDetailsVM::class.java) -> SettingsDetailsVM(pricesRepo)
                    else -> throw IllegalArgumentException("Don't know how to create ${modelClass.simpleName}")
                }
            } as T
}
