package pl.srw.billcalculator.data.bill

import android.app.backup.BackupManager
import android.content.Context
import android.support.annotation.CheckResult
import io.reactivex.Single
import org.greenrobot.greendao.query.LazyList
import pl.srw.billcalculator.bill.SavedBillsRegistry
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.History
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.persistence.type.BillType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepo @Inject constructor(
    private val applicationContext: Context,
    private val savedBillsRegistry: SavedBillsRegistry
) {

    private var deleted: Collection<Pair<Bill, Prices>> = emptyList()

    // FIXME: Rewrite to Rx
    fun getAll(): LazyList<History> = Database.getHistory()

    @CheckResult
    fun insert(bill: Bill): Single<Bill> = Single.fromCallable {
        Database.insert(bill)
        BackupManager(applicationContext).dataChanged()
        savedBillsRegistry.register(bill)
        bill
    }

    @CheckResult
    fun insert(prices: Prices): Single<Prices> = Single.fromCallable<Prices> {
        Database.insert(prices)
        prices
    }

//    fun insertNew(prices: Prices, bull: Bill) { bill.pricesId = prices.id } // FIXME: single call for save with backup update

    fun deleteBillsWithPrices(bills: Collection<Bill>) {
        bills.forEach { deleteBillWithPrices(it) }
    }

    fun deleteBillWithPrices(bill: Bill) {
        Database.deleteBillWithPrices(BillType.valueOf(bill), bill.id, bill.pricesId)
    }

    fun cacheBillsForUndoDelete(bills: Collection<Bill>) {
        val prices = bills.map { loadPricesForBill(it) }
        deleted = bills.zip(prices)
    }

    fun cacheBillForUndoDelete(bill: Bill) {
        deleted = listOf(Pair(bill, loadPricesForBill(bill)))
    }

    fun undoDelete() {
        for ((bill, prices) in deleted) {
            Database.insert(prices)
            Database.insert(bill)
        }
        deleted = emptyList()
    }

    fun isUndoDeletePossible() = !deleted.isEmpty()

    private fun loadPricesForBill(bill: Bill): Prices {
        val billType = BillType.valueOf(bill)
        val loadPrices = Database.loadPrices(billType, bill.pricesId)
        return checkNotNull(loadPrices, { "Prices not found for id ${bill.pricesId}" })
    }
}
