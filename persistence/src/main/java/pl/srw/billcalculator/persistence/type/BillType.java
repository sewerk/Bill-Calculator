package pl.srw.billcalculator.persistence.type;

import de.greenrobot.dao.AbstractDao;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.persistence.exception.DbNotInitializedYetException;

/**
 * Created by Kamil Seweryn.
 */
public enum BillType {

    PGE_G11,
    PGE_G12,
    PGNIG;

    public AbstractDao<? extends Bill, Long> getDao() {
        assertNotNull(Database.getSession());
        switch (this) {
            case PGE_G11:
                return Database.getSession().getPgeG11BillDao();
            case PGE_G12:
                return Database.getSession().getPgeG12BillDao();
            case PGNIG:
                return Database.getSession().getPgnigBillDao();
        }
        throw new RuntimeException("Type " + this + " not handled.");
    }

    private void assertNotNull(final DaoSession session) {
        if (session == null)
            throw new DbNotInitializedYetException();
    }

}
