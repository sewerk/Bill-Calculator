package pl.srw.billcalculator.history

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.db.PgeG11Bill
import pl.srw.billcalculator.db.PgeG12Bill
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.settings.prices.PgePrices
import pl.srw.billcalculator.util.Dates
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryGenerator @Inject constructor(val prices: PgePrices) {

    companion object {
        @JvmStatic
        fun clear() {
            Timber.d("Clear Database")
            val session = Database.getSession()
            //bills
            session.pgnigBillDao.deleteAll()
            session.pgeG11BillDao.deleteAll()
            session.pgeG12BillDao.deleteAll()
            session.tauronG11BillDao.deleteAll()
            session.tauronG12BillDao.deleteAll()
            //prices
            session.pgnigPricesDao.deleteAll()
            session.pgePricesDao.deleteAll()
            session.tauronPricesDao.deleteAll()
        }
    }

    fun generatePgeG11Bill(readingTo: Int) {
        Timber.d("Create PGE G11 bill with readingTo=" + readingTo)
        insertBill(readingTo, insertPrices().id)
    }

    fun generatePgeG12Bill(readingDayTo: Int, readingNightTo: Int = 211) {
        Timber.d("Create PGE G12 bill with readingTo=" + readingDayTo)
        insertBill(readingDayTo, readingNightTo, insertPrices().id)
    }

    fun generatePgeG11Bills(count: Int) {
        Timber.d("Generate $count PGE G11 bills")
        (1..count).forEach { i -> insertBill(i + 10, insertPrices().id)}
    }

    private fun insertBill(readingTo: Int, id: Long) {
        val day = if (readingTo % 335 == 0) 1 else readingTo % 335
        val fromDate = Dates.toDate(LocalDate.ofYearDay(2017, day))
        val toDate = Dates.toDate(LocalDate.ofYearDay(2017, day + 30))

        val readingFrom = if (readingTo - 10 < 0) 0 else readingTo - 10
        val bill = PgeG11Bill(null, readingFrom, readingTo, fromDate, toDate, readingTo * 11.11, id)
        Database.getSession().insert(bill)
    }

    private fun insertBill(readingDayTo: Int, readingNightTo: Int, id: Long) {
        val day = if (readingDayTo % 335 == 0) 1 else readingDayTo % 335
        val fromDate = Dates.toDate(LocalDate.ofYearDay(2017, day))
        val toDate = Dates.toDate(LocalDate.ofYearDay(2017, day + 30))

        val readingDayFrom = if (readingDayTo - 10 < 0) 0 else readingDayTo - 10
        val readingNightFrom = if (readingNightTo - 10 < 0) 0 else readingNightTo - 10
        val bill = PgeG12Bill(null, readingDayFrom, readingDayTo, readingNightFrom, readingNightTo,
                fromDate, toDate, readingDayTo * 11.11, id)
        Database.getSession().insert(bill)
    }

    private fun insertPrices(): pl.srw.billcalculator.db.PgePrices {
        val prices = prices.convertToDb()
        Database.getSession().insert(prices)
        return prices
    }
}