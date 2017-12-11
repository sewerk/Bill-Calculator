package pl.srw.billcalculator.intent

import android.content.Context
import android.content.Intent
import org.threeten.bp.LocalDate
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.PgeG11Bill
import pl.srw.billcalculator.db.PgeG12Bill
import pl.srw.billcalculator.db.PgnigBill
import pl.srw.billcalculator.db.TauronG11Bill
import pl.srw.billcalculator.db.TauronG12Bill
import pl.srw.billcalculator.form.FormVM
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.wrapper.Analytics

class IntentCreator internal constructor(context: Context, aClass: Class<*>) {
    private val intent: Intent = Intent(context, aClass)

    fun from(vm: FormVM): Intent {
        return if (vm.isSingleReadingTariff()) {
            from(vm.readingFrom.get().toInt(), vm.readingTo.get().toInt(),
                    parse(vm.dateFrom.get()), parse(vm.dateTo.get()))
        } else {
            from(vm.readingDayFrom.get().toInt(), vm.readingDayTo.get().toInt(),
                    vm.readingNightFrom.get().toInt(), vm.readingNightTo.get().toInt(),
                    parse(vm.dateFrom.get()), parse(vm.dateTo.get()))
        }
    }

    private fun from(readingFrom: Int, readingTo: Int, dateFrom: LocalDate, dateTo: LocalDate): Intent {
        putReadingsExtra(readingFrom, readingTo)
        putDatesExtra(dateFrom, dateTo)
        return intent
    }

    private fun from(readingDayFrom: Int, readingDayTo: Int, readingNightFrom: Int, readingNightTo: Int, dateFrom: LocalDate, dateTo: LocalDate): Intent {
        putReadingsG12Extra(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo)
        putDatesExtra(dateFrom, dateTo)
        return intent
    }

    fun from(bill: PgeG11Bill): Intent {
        putDatesExtra(bill)
        putReadingsExtra(bill.readingFrom!!, bill.readingTo!!)
        intent.putExtra(PRICES, bill.pgePrices)
        return intent
    }

    fun from(bill: PgeG12Bill): Intent {
        putDatesExtra(bill)
        putReadingsG12Extra(bill.readingDayFrom!!, bill.readingDayTo!!, bill.readingNightFrom!!, bill.readingNightTo!!)
        intent.putExtra(PRICES, bill.pgePrices)
        return intent
    }

    fun from(bill: PgnigBill): Intent {
        putDatesExtra(bill)
        putReadingsExtra(bill.readingFrom!!, bill.readingTo!!)
        intent.putExtra(PRICES, bill.pgnigPrices)
        return intent
    }

    fun from(bill: TauronG11Bill): Intent {
        putDatesExtra(bill)
        putReadingsExtra(bill.readingFrom!!, bill.readingTo!!)
        intent.putExtra(PRICES, bill.tauronPrices)
        return intent
    }

    fun from(bill: TauronG12Bill): Intent {
        putDatesExtra(bill)
        putReadingsG12Extra(bill.readingDayFrom!!, bill.readingDayTo!!, bill.readingNightFrom!!, bill.readingNightTo!!)
        intent.putExtra(PRICES, bill.tauronPrices)
        return intent
    }

    private fun putDatesExtra(bill: Bill) {
        val dateFrom = Dates.toLocalDate(bill.dateFrom)
        val dateTo = Dates.toLocalDate(bill.dateTo)
        putDatesExtra(dateFrom, dateTo)
    }

    private fun putReadingsExtra(readingFrom: Int, readingTo: Int) {
        intent.putExtra(READING_FROM, readingFrom)
        intent.putExtra(READING_TO, readingTo)
        Analytics.setInt(READING_FROM, readingFrom)
        Analytics.setInt(READING_TO, readingTo)
    }

    private fun putReadingsG12Extra(readingDayFrom: Int, readingDayTo: Int, readingNightFrom: Int, readingNightTo: Int) {
        intent.putExtra(READING_DAY_FROM, readingDayFrom)
        intent.putExtra(READING_DAY_TO, readingDayTo)
        intent.putExtra(READING_NIGHT_FROM, readingNightFrom)
        intent.putExtra(READING_NIGHT_TO, readingNightTo)
        Analytics.setInt(READING_DAY_FROM, readingDayFrom)
        Analytics.setInt(READING_DAY_TO, readingDayTo)
        Analytics.setInt(READING_NIGHT_FROM, readingNightFrom)
        Analytics.setInt(READING_NIGHT_TO, readingNightTo)
    }

    private fun putDatesExtra(dateFrom: LocalDate, dateTo: LocalDate) {
        intent.putExtra(DATE_FROM, dateFrom)
        intent.putExtra(DATE_TO, dateTo)
        Analytics.setString(DATE_FROM, dateFrom.toString())
        Analytics.setString(DATE_TO, dateTo.toString())
    }

    private fun parse(text: String): LocalDate {
        return Dates.parse(text, FormFragment.DATE_PATTERN)
    }

    companion object {

        const val READING_FROM = "READING_FROM"
        const val READING_TO = "READING_TO"
        const val READING_DAY_FROM = "READING_DAY_FROM"
        const val READING_DAY_TO = "READING_DAY_TO"
        const val READING_NIGHT_FROM = "READING_NIGHT_FROM"
        const val READING_NIGHT_TO = "READING_NIGHT_TO"
        const val DATE_FROM = "DATE_FROM"
        const val DATE_TO = "DATE_TO"
        const val PRICES = "PRICES"
    }
}
