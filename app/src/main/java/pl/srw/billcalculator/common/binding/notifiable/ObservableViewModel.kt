package pl.srw.billcalculator.common.binding.notifiable

import android.arch.lifecycle.ViewModel

/**
 * Base [ViewModel] class with ability to notify changes on view bindings
 */
open class ObservableViewModel(notifiableObservable: NotifiableObservable = BaseNotifiableObservable())
    : ViewModel(), NotifiableObservable by notifiableObservable {

    init {
        setDelegator(this)
    }
}
