package pl.srw.billcalculator.wrapper

import android.util.Pair
import org.greenrobot.greendao.query.LazyList
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.History
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.persistence.type.BillType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepo @Inject internal constructor() {

    var deleted: Collection<Pair<Bill, Prices>> = emptyList()

    // TODO: Rewrite to Rx
    fun getAll(): LazyList<History> = Database.getHistory()

    fun deleteBillWithPrices(bill: Bill) {
        Database.deleteBillWithPrices(BillType.valueOf(bill), bill.id, bill.pricesId)
    }

    fun cacheBillsForUndoDelete(bills: Collection<Bill>) {
        deleted = bills.map(this::load)
    }

    fun cacheBillForUndoDelete(bill: Bill) {
        deleted = listOf(load(bill))
    }

    fun undoDelete() {
        for (pair in deleted) {
            Database.insert(pair.first, pair.second)
        }
        deleted = emptyList()
    }

    fun isUndoDeletePossible()= !deleted.isEmpty()

    private fun load(bill: Bill) = Database.load(BillType.valueOf(bill), bill.id, bill.pricesId)
}
