package pl.srw.billcalculator.persistence.type;

import de.greenrobot.dao.AbstractDao;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.persistence.Database;

/**
 * Created by Kamil Seweryn.
 */
public enum BillType {

    PGE(Database.getSession().getPgeBillDao()),
    PGE_G12(Database.getSession().getPgeG12BillDao()),
    PGNIG(Database.getSession().getPgnigBillDao());

    public AbstractDao<? extends Bill, Long> dao;

    BillType(AbstractDao<? extends Bill, Long> dao) {
        this.dao = dao;
    }
}
