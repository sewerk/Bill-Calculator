package pl.srw.billcalculator.form.fragment

import pl.srw.billcalculator.form.FormVM
import pl.srw.billcalculator.form.FormValueValidator
import pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect
import pl.srw.billcalculator.form.FormValueValidator.isValueFilled
import pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect
import pl.srw.billcalculator.util.analytics.EventType
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.type.Provider.PGNIG
import pl.srw.billcalculator.util.analytics.Analytics
import timber.log.Timber

class FormPresenter(private val view: FormView,
                    private val provider: Provider,
                    private val historyUpdater: HistoryChangeListener) {

    fun closeButtonClicked() {
        Timber.i("Form: Close clicked")
        view.hideForm()
    }

    fun calculateButtonClicked(vm: FormVM) {
        Timber.i("Form: Calculate clicked")
        view.cleanErrorsOnFields()

        val singleReadings = provider == PGNIG || vm.isSingleReadingsProcessing()
        val validInput = if (singleReadings) {
            isSingleReadingsFormValid(vm.readingFrom, vm.readingTo, vm.dateFrom, vm.dateTo)
        } else {
            isDoubleReadingsFormValid(vm.readingDayFrom, vm.readingDayTo,
                    vm.readingNightFrom, vm.readingNightTo, vm.dateFrom, vm.dateTo)
        }
        if (!validInput) return

        with(view) {
            startStoringService(provider)
            startBillActivity(provider)
            hideForm()
        }
        historyUpdater.onHistoryChanged()
        Analytics.event(EventType.CALCULATE, "provider", provider)
    }

    private fun isSingleReadingsFormValid(readingFrom: String, readingTo: String,
                                          dateFrom: String, dateTo: String): Boolean {
        return (isValueFilled(readingFrom, onErrorCallback(FormView.Field.READING_FROM))
                && isValueFilled(readingTo, onErrorCallback(FormView.Field.READING_TO))
                && isValueOrderCorrect(readingFrom, readingTo, onErrorCallback(FormView.Field.READING_TO))
                && isDatesOrderCorrect(dateFrom, dateTo, onDateErrorCallback()))

    }

    private fun isDoubleReadingsFormValid(readingDayFrom: String, readingDayTo: String,
                                          readingNightFrom: String, readingNightTo: String,
                                          dateFrom: String, dateTo: String): Boolean {
        return (isValueFilled(readingDayFrom, onErrorCallback(FormView.Field.READING_DAY_FROM))
                && isValueFilled(readingDayTo, onErrorCallback(FormView.Field.READING_DAY_TO))
                && isValueFilled(readingNightFrom, onErrorCallback(FormView.Field.READING_NIGHT_FROM))
                && isValueFilled(readingNightTo, onErrorCallback(FormView.Field.READING_NIGHT_TO))
                && isValueOrderCorrect(readingDayFrom, readingDayTo, onErrorCallback(FormView.Field.READING_DAY_TO))
                && isValueOrderCorrect(readingNightFrom, readingNightTo, onErrorCallback(FormView.Field.READING_NIGHT_TO))
                && isDatesOrderCorrect(dateFrom, dateTo, onDateErrorCallback()))

    }

    private fun onErrorCallback(field: FormView.Field) = FormValueValidator.OnErrorCallback { view.showReadingFieldError(field, it) }

    private fun onDateErrorCallback() = FormValueValidator.OnErrorCallback(view::showDateFieldError)

    interface FormView {

        fun showProviderSettings(provider: Provider)

        fun hideForm()

        fun showReadingFieldError(field: Field, errorMsgRes: Int)

        fun showDateFieldError(errorMsgRes: Int)

        fun cleanErrorsOnFields()

        fun startStoringService(provider: Provider)

        fun startBillActivity(provider: Provider)

        enum class Field {
            READING_FROM, READING_TO,
            READING_DAY_FROM, READING_DAY_TO,
            READING_NIGHT_FROM, READING_NIGHT_TO
        }
    }

    interface HistoryChangeListener {
        fun onHistoryChanged()
    }
}
