package pl.srw.billcalculator.form

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pl.srw.billcalculator.wrapper.PricesRepo
import pl.srw.billcalculator.wrapper.ReadingsRepo
import javax.inject.Inject

class FormVMFactory @Inject constructor(private val readingsRepo: ReadingsRepo,
                                        private val pricesRepo: PricesRepo) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == FormVM::class.java) return FormVM(readingsRepo, pricesRepo) as T
        else throw  IllegalArgumentException("Don't know how to create ${modelClass.simpleName}")
    }
}
