package pl.srw.billcalculator.form

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import pl.srw.billcalculator.data.bill.ReadingsRepo
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.type.Provider
import javax.inject.Inject

class FormVMFactory @Inject constructor(private val readingsRepo: ReadingsRepo,
                                        private val pricesRepo: PricesRepo) : ViewModelProvider.Factory {

    lateinit var provider: Provider

    var bundle: Bundle? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(FormVM::class.java) -> createFormVM()
                isAssignableFrom(FormPreviousReadingsVM::class.java) -> FormPreviousReadingsVM(provider, readingsRepo, pricesRepo)
                else -> throw IllegalArgumentException("Don't know how to create ${modelClass.simpleName}")
            }
        } as T

    private fun createFormVM() = FormVM(provider, pricesRepo).apply {
        bundle?.let { readFrom(it) }
    }
}
