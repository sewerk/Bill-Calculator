package pl.srw.billcalculator.db;

import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.test.AbstractDaoSessionTest;

import java.util.Date;
import java.util.List;

import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.persistence.Triggers;
import pl.srw.billcalculator.persistence.type.BillType;

public class TriggersTest extends AbstractDaoSessionTest<DaoMaster, DaoSession> {

    public TriggersTest() {
        super(DaoMaster.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        Triggers.create(((StandardDatabase) db).getSQLiteDatabase());
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

    public void testTriggerInsertHistoryEntryOnInsertTauronG11Bill() {
        testTriggerInsertHistoryEntryOnInsert(new TauronG11Bill(), BillType.TAURON_G11);
    }

    public void testTriggerInsertHistoryEntryOnInsertTauronG12Bill() {
        testTriggerInsertHistoryEntryOnInsert(new TauronG12Bill(), BillType.TAURON_G12);
    }

    public void testTriggerDeleteHistoryEntryOnDeletePgeBill() {
        testTriggerDeleteHistoryEntryOnDelete(new PgeG11Bill());
    }

    public void testTriggerDeleteHistoryEntryOnDeletePgeG12Bill() {
        testTriggerDeleteHistoryEntryOnDelete(new PgeG12Bill());
    }

    public void testTriggerDeleteHistoryEntryOnDeleteTauronG11Bill() {
        testTriggerDeleteHistoryEntryOnDelete(new TauronG11Bill());
    }

    public void testTriggerDeleteHistoryEntryOnDeleteTauronG12Bill() {
        testTriggerDeleteHistoryEntryOnDelete(new TauronG12Bill());
    }

    public void testTriggerDeleteHistoryEntryOnDeletePgnigBill() {
        testTriggerDeleteHistoryEntryOnDelete(new PgnigBill());
    }

    public void testTriggerDeleteSingleHistoryEntryOnDeletePgeG12Bill() {
        final PgeG11Bill bill = new PgeG11Bill(1L);
        // given: 3 bills inserted
        daoSession.insert(bill);
        daoSession.insert(new PgeG12Bill(1L));
        daoSession.insert(new PgnigBill(1L));

        // when: 1 bill is deleted
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
