package pl.srw.billcalculator.wrapper;

import org.greenrobot.greendao.query.LazyList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.persistence.type.BillType;

@Singleton
public class HistoryRepo {

    @Inject
    HistoryRepo() {
    }

    public void deleteBillWithPrices(BillType billType, Long id, Long pricesId) {
        Database.deleteBillWithPrices(billType, id, pricesId);
    }

    public void undoDelete() {
        Database.undelete();
    }

    public LazyList<History> getAll() {
        return Database.getHistory(); // TODO: Rewrite to Rx
    }
}
