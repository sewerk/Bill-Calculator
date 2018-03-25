package pl.srw.billcalculator.data.bill

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

    private var deleted: Collection<Pair<Bill, Prices>> = emptyList()

    // FIXME: Rewrite to Rx
    fun getAll(): LazyList<History> = Database.getHistory()

    fun insert(bill: Bill): Bill {
        Database.insert(bill)
        return bill
    }

    fun insert(prices: Prices): Prices {
        Database.insert(prices)
        return prices
    }

    fun deleteBillsWithPrices(bills: Collection<Bill>) {
        bills.forEach { deleteBillWithPrices(it) }
    }

    fun deleteBillWithPrices(bill: Bill) {
        Database.deleteBillWithPrices(BillType.valueOf(bill), bill.id, bill.pricesId)
    }

    fun cacheBillsForUndoDelete(bills: Collection<Bill>) {
        val prices = bills.map { loadPricesForBill(it)}
        deleted = bills.zip(prices)
    }

    fun cacheBillForUndoDelete(bill: Bill) {
        deleted = listOf(Pair(bill, loadPricesForBill(bill)))
    }

    fun undoDelete() {
        for ((bill, prices) in deleted) {
            insert(prices)
            insert(bill)
        }
        deleted = emptyList()
    }

    fun isUndoDeletePossible()= !deleted.isEmpty()

    private fun loadPricesForBill(bill: Bill): Prices {
        val billType = BillType.valueOf(bill)
        val loadPrices = Database.loadPrices(billType, bill.pricesId)
        return checkNotNull(loadPrices, {"Prices not found for id ${bill.pricesId}"})
    }
}
