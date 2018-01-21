package pl.srw.billcalculator.common.binding.notifiable

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
/**
 * An abstraction of [BaseObservable] for composition purposes.
 *
 * The framework class [BaseObservable] forces inheritance.
 *
 * To use, a view model class should implement this and delegate to an instance of [BaseNotifiableObservable] like so:
 *
 * ```
 *  class ViewModel(
 *      // provide implementation as a default parameter, so a mock can be provided for tests.
 *      notifiableObservable: NotifiableObservable = BaseNotifiableObservable()
 *  ) : NotifiableObservable by notifiableObservable {
 *
 *      init {
 *          // the delegatee requires a reference to it's delegator
 *          setDelegator(this)
 *      }
 *  }
 * ```
 *
 */
interface NotifiableObservable : Observable {

    /**
     * Must be called on initialisation. See [NotifiableObservable] for usage.
     */
    fun setDelegator(notifiableObservable: NotifiableObservable)

    /**
     * Notifies listeners that a specific property has changed.
     *
     * The getter for the property that changes should be marked with [Bindable]
     * so that field is generated in your `BR` class to be used as [propertyId].
     *
     * @param propertyId The generated BR id for the [Bindable] field.
     */
    fun notifyPropertyChanged(propertyId: Int)

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange()
}
