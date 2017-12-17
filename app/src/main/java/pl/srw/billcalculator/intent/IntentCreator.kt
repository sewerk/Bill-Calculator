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
import timber.log.Timber

@Suppress("TooManyFunctions")
class IntentCreator internal constructor(context: Context, aClass: Class<*>) {

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

    private val intent: Intent = Intent(context, aClass)

    fun from(vm: FormVM): Intent {
        return if (vm.isSingleReadingsProcessing()) {
            from(vm.readingFrom.toInt(), vm.readingTo.toInt(),
                    parse(vm.dateFrom), parse(vm.dateTo))
        } else {
            from(vm.readingDayFrom.toInt(), vm.readingDayTo.toInt(),
                    vm.readingNightFrom.toInt(), vm.readingNightTo.toInt(),
                    parse(vm.dateFrom), parse(vm.dateTo))
        }
    }

    private fun from(readingFrom: Int, readingTo: Int,
                     dateFrom: LocalDate, dateTo: LocalDate): Intent {
        putReadingsExtra(readingFrom, readingTo)
        putDatesExtra(dateFrom, dateTo)
        return intent
    }

    private fun from(readingDayFrom: Int, readingDayTo: Int,
                     readingNightFrom: Int, readingNightTo: Int,
                     dateFrom: LocalDate, dateTo: LocalDate): Intent {
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
        Timber.i("$READING_FROM=$readingFrom, $READING_TO=$readingTo")
    }

    private fun putReadingsG12Extra(readingDayFrom: Int, readingDayTo: Int, readingNightFrom: Int, readingNightTo: Int) {
        intent.putExtra(READING_DAY_FROM, readingDayFrom)
        intent.putExtra(READING_DAY_TO, readingDayTo)
        intent.putExtra(READING_NIGHT_FROM, readingNightFrom)
        intent.putExtra(READING_NIGHT_TO, readingNightTo)
        Timber.i("$READING_DAY_FROM=$readingDayFrom, $READING_DAY_TO=$readingDayTo, " +
                "$READING_NIGHT_FROM=$readingNightFrom, $READING_NIGHT_TO=$readingNightTo")
    }

    private fun putDatesExtra(dateFrom: LocalDate, dateTo: LocalDate) {
        intent.putExtra(DATE_FROM, dateFrom)
        intent.putExtra(DATE_TO, dateTo)
        Timber.i("$DATE_FROM=$dateFrom, $DATE_TO=$dateTo")
    }

    private fun parse(text: String): LocalDate {
        return Dates.parse(text, FormFragment.DATE_PATTERN)
    }
}
