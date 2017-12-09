package pl.srw.billcalculator.form.fragment

import pl.srw.billcalculator.form.FormVM
import pl.srw.billcalculator.form.FormValueValidator
import pl.srw.billcalculator.form.FormValueValidator.isDatesOrderCorrect
import pl.srw.billcalculator.form.FormValueValidator.isValueFilled
import pl.srw.billcalculator.form.FormValueValidator.isValueOrderCorrect
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.ActionType
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.type.Provider.PGNIG
import pl.srw.billcalculator.wrapper.Analytics

class FormPresenter(private val view: FormView,
                    private val provider: Provider,
                    private val historyUpdater: HistoryChangeListener) {

    fun closeButtonClicked() {
        Analytics.log("Form: Close clicked")
        view.hideForm()
    }

    fun calculateButtonClicked(vm: FormVM) {
        Analytics.log("Form: Calculate clicked")
        view.cleanErrorsOnFields()

        if (provider == PGNIG || vm.isSingleReadingTariff()) {
            if (!isSingleReadingsFormValid(vm.readingFrom.get(), vm.readingTo.get(), vm.dateFrom.get(), vm.dateTo.get())) {
                return
            }
            view.startStoringServiceForSingleReadings(provider)
            view.startBillActivityForSingleReadings(provider)
            view.hideForm()
        } else {
            if (!isDoubleReadingsFormValid(vm.readingDayFrom.get(), vm.readingDayTo.get(),
                    vm.readingNightFrom.get(), vm.readingNightTo.get(), vm.dateFrom.get(), vm.dateTo.get())) {
                return
            }
            view.startStoringServiceForDoubleReadings(provider)
            view.startBillActivityForDoubleReadings(provider)
            view.hideForm()
        }
        historyUpdater.onHistoryChanged()
        Analytics.logAction(ActionType.CALCULATE, "provider", provider)
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

    private fun FormVM.isSingleReadingTariff(): Boolean = SharedPreferencesEnergyPrices.TARIFF_G11 == tariffLabel

    private fun onErrorCallback(field: FormView.Field) = FormValueValidator.OnErrorCallback { view.showReadingFieldError(field, it) }

    private fun onDateErrorCallback() = FormValueValidator.OnErrorCallback(view::showDateFieldError)

    interface FormView {

        fun hideForm()

        fun showReadingFieldError(field: Field, errorMsgRes: Int)

        fun showDateFieldError(errorMsgRes: Int)

        fun cleanErrorsOnFields()

        fun startStoringServiceForSingleReadings(provider: Provider)

        fun startBillActivityForSingleReadings(provider: Provider)

        fun startStoringServiceForDoubleReadings(provider: Provider)

        fun startBillActivityForDoubleReadings(provider: Provider)

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
