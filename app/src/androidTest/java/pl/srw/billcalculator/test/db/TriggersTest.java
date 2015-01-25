package pl.srw.billcalculator.test.db;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.test.AbstractDaoSessionTest;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeBill;
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

    public void testSaveTriggerForPgeBill() {
        testSaveTriggerFor(new PgeG12Bill(), BillType.PGE_G12);
    }

    public void testSaveTriggerForPgeG12Bill() {
        testSaveTriggerFor(new PgeBill(), BillType.PGE);
    }

    public void testSaveTriggerForPgnigBill() {
        testSaveTriggerFor(new PgnigBill(), BillType.PGNIG);
    }

    public void testDeleteTriggerForPgeBill() {
        testDeleteTriggerFor(new PgeBill());
    }

    public void testDeleteTriggerForPgeG12Bill() {
        testDeleteTriggerFor(new PgeG12Bill());
    }

    public void testDeleteTriggerForPgnigBill() {
        testDeleteTriggerFor(new PgnigBill());
    }

    private void testDeleteTriggerFor(Bill bill) {
        // save
        daoSession.insert(bill);

        // delete
        daoSession.delete(bill);

        // test
        assertTrue(daoSession.getHistoryDao().loadAll().isEmpty());
    }

    private void testSaveTriggerFor(Bill bill, BillType billType) {
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
