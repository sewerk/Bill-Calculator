package pl.srw.billcalculator.settings

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import pl.srw.billcalculator.data.settings.SettingsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsVMFactory @Inject constructor(private val settingsRepo: SettingsRepo) : ViewModelProvider.Factory {

    var bundle: Bundle? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SettingsVM::class.java) -> createSettingsVM()
                    else -> throw IllegalArgumentException("Don't know how to create ${modelClass.simpleName}")
                }
            } as T

    private fun createSettingsVM() = SettingsVM(settingsRepo).apply {
        bundle?.let { readFrom(it) }
    }
}
