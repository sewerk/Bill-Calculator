package pl.srw.billcalculator.wrapper

import org.greenrobot.greendao.query.LazyList
import pl.srw.billcalculator.db.History
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.persistence.type.BillType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepo @Inject internal constructor() {

    fun deleteBillWithPrices(billType: BillType, id: Long, pricesId: Long) {
        Database.deleteBillWithPrices(billType, id, pricesId)
    }

    fun cacheBillForUndoDelete(billType: BillType, id: Long, pricesId: Long) {
        Database.cacheForUndelete(billType, id, pricesId)
    }

    fun undoDelete() {
        Database.undelete()
    }

    fun isUndoDeletePossible(): Boolean = Database.canUndelete()

    // TODO: Rewrite to Rx
    fun getAll(): LazyList<History> = Database.getHistory()
}
