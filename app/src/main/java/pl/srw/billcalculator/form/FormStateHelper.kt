package pl.srw.billcalculator.form

import android.os.Bundle

/**
 * Helps with saving and restoring form state
 */
object FormStateHelper {
    private const val FROM = "form.state.from"
    private const val TO = "form.state.to"
    private const val DATE_FROM = "form.state.date_from"
    private const val DATE_TO = "form.state.date_to"
    private const val DAY_FROM = "from.state.day_from"
    private const val DAY_TO = "form.state.day_to"
    private const val NIGHT_FROM = "form.state.night_from"
    private const val NIGHT_TO = "form.state.night_to"

    fun put(target: Bundle, vm: FormVM) {
        target.putString(FROM, vm.readingFrom)
        target.putString(TO, vm.readingTo)
        target.putString(DATE_FROM, vm.dateFrom)
        target.putString(DATE_TO, vm.dateTo)
        target.putString(DAY_FROM, vm.readingDayFrom)
        target.putString(DAY_TO, vm.readingDayTo)
        target.putString(NIGHT_FROM,vm.readingNightFrom)
        target.putString(NIGHT_TO, vm.readingNightTo)
    }

    fun retrieve(bundle: Bundle, vm: FormVM) {
        vm.readingFrom = bundle.getString(FROM)
        vm.readingTo = bundle.getString(TO)
        vm.dateFrom = bundle.getString(DATE_FROM)
        vm.dateTo = bundle.getString(DATE_TO)
        vm.readingDayFrom = bundle.getString(DAY_FROM)
        vm.readingDayTo = bundle.getString(DAY_TO)
        vm.readingNightFrom = bundle.getString(NIGHT_FROM)
        vm.readingNightTo = bundle.getString(NIGHT_TO)
    }
}
