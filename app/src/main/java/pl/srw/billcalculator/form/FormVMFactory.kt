package pl.srw.billcalculator.form

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.wrapper.PricesRepo
import pl.srw.billcalculator.wrapper.ReadingsRepo
import javax.inject.Inject

class FormVMFactory @Inject constructor(private val readingsRepo: ReadingsRepo,
                                        private val pricesRepo: PricesRepo) : ViewModelProvider.Factory {

    lateinit var provider: Provider

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(FormVM::class.java) -> FormVM(provider, pricesRepo)
                isAssignableFrom(FormPreviousReadingsVM::class.java) -> FormPreviousReadingsVM(provider, readingsRepo, pricesRepo)
                else -> throw IllegalArgumentException("Don't know how to create ${modelClass.simpleName}")
            }
        } as T
}
