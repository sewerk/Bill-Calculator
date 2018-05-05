package pl.srw.billcalculator.data.bill

import android.app.backup.BackupManager
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.annotation.CheckResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import pl.srw.billcalculator.bill.SavedBillsRegistry
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.History
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.persistence.type.BillType
import javax.inject.Inject
import javax.inject.Singleton

@SuppressWarnings("TooManyFunctions")
@Singleton
class HistoryRepo @Inject constructor(
    private val applicationContext: Context,
    private val savedBillsRegistry: SavedBillsRegistry
) {

    private val all = MutableLiveData<List<History>>()
    private var deleted: Collection<Pair<Bill, Prices>> = emptyList()

    init {
        loadAllAsync()
    }

    fun getAll(): LiveData<List<History>> = all

    @CheckResult
    fun insert(bill: Bill): Bill {
        Database.insert(bill)
        loadAllAsync()
        BackupManager(applicationContext).dataChanged()
        savedBillsRegistry.register(bill)
        return bill
    }

    @CheckResult
    fun insert(prices: Prices): Prices {
        Database.insert(prices)
        return prices
    }

//    fun insertNew(prices: Prices, bull: Bill) { bill.pricesId = prices.id } // FIXME: single call for save with backup update

    fun deleteBillsWithPrices(bills: Collection<Bill>) {
        bills.forEach { deleteSingleBillWithPrices(it) }
        loadAllAsync()
    }

    fun deleteBillWithPrices(bill: Bill) {
        deleteSingleBillWithPrices(bill, true)
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
        loadAllAsync()
    }

    fun isUndoDeletePossible() = !deleted.isEmpty()

    private fun deleteSingleBillWithPrices(bill: Bill, updateObserver: Boolean = false) {
        Database.deleteBillWithPrices(BillType.valueOf(bill), bill.id, bill.pricesId)
        if (updateObserver) loadAllAsync()
    }

    private fun loadAllAsync() {
        Single.fromCallable { Database.getHistory() }
            .subscribeOn(AndroidSchedulers.mainThread()) // FIXME: GreenDao requires single thread
            .subscribe { list -> all.value = list }
    }

    private fun loadPricesForBill(bill: Bill): Prices {
        val billType = BillType.valueOf(bill)
        val loadPrices = Database.loadPrices(billType, bill.pricesId)
        return checkNotNull(loadPrices, { "Prices not found for id ${bill.pricesId}" })
    }
}
