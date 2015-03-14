package pl.srw.billcalculator.test.db;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.test.AbstractDaoSessionTest;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.persistence.Triggers;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
public class TriggersTest extends AbstractDaoSessionTest<BillCalculator, DaoMaster, DaoSession> {

    public TriggersTest() {
        super(DaoMaster.class);
    }

    @Override
    protected void setUp() {
        super.setUp();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        Triggers.create(db);
    }

    public void testTriggerInsertHistoryEntryOnInsertPgeBill() {
        testTriggerInsertHistoryEntryOnInsert(new PgeG12Bill(), BillType.PGE_G12);
    }

    public void testTriggerInsertHistoryEntryOnInsertPgeG12Bill() {
        testTriggerInsertHistoryEntryOnInsert(new PgeG11Bill(), BillType.PGE_G11);
    }

    public void testTriggerInsertHistoryEntryOnInsertPgnigBill() {
        testTriggerInsertHistoryEntryOnInsert(new PgnigBill(), BillType.PGNIG);
    }

    public void testTriggerDeleteHistoryEntryOnDeletePgeBill() {
        testTriggerDeleteHistoryEntryOnDelete(new PgeG11Bill());
    }

    public void testTriggerDeleteHistoryEntryOnDeletePgeG12Bill() {
        testTriggerDeleteHistoryEntryOnDelete(new PgeG12Bill());
    }

    public void testTriggerDeleteHistoryEntryOnDeletePgnigBill() {
        testTriggerDeleteHistoryEntryOnDelete(new PgnigBill());
    }

    public void testTriggerDeleteSingleHistoryEntryOnDeletePgeG12Bill() {
        final PgeG11Bill bill = new PgeG11Bill(1L);
        // when: 3 bills inserted
        daoSession.insert(bill);
        daoSession.insert(new PgeG12Bill(1L));
        daoSession.insert(new PgnigBill(1L));

        // given: 1 bill is deleted
        daoSession.delete(bill);

        // then: 2 history entries present
        assertEquals(2, daoSession.getHistoryDao().loadAll().size());
    }

    private void testTriggerDeleteHistoryEntryOnDelete(Bill bill) {
        // save
        daoSession.insert(bill);

        // delete
        daoSession.delete(bill);

        // test
        assertTrue(daoSession.getHistoryDao().loadAll().isEmpty());
    }

    private void testTriggerInsertHistoryEntryOnInsert(Bill bill, BillType billType) {
        Date date = new Date();
        // save
        bill.setDateFrom(date);
        final long billId = daoSession.insert(bill);

        // load
        final List<History> history = daoSession.getHistoryDao().loadAll();

        // test
        assertEquals(1, history.size());
        final History historyBill = history.get(0);
        assertEquals(billId, historyBill.getBillId().longValue());
        assertEquals(date, historyBill.getDateFrom());
        assertEquals(billType.toString(), historyBill.getBillType());
    }
}
