package pl.srw.billcalculator.test.db;

import java.util.List;

import de.greenrobot.dao.query.LazyList;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.test.AbstractDaoSessionTest;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.PgnigPrices;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;

/**
 * Created by Kamil Seweryn.
 */
public class DatabaseTest extends AbstractDaoSessionTest<BillCalculator, DaoMaster, DaoSession> {

    public DatabaseTest() {
        super(DaoMaster.class);
    }

    @Override
    protected void setUp() {
        super.setUp();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public void testLoadById() {
        // save
        final PgePrices prices = new PgePrices();
        daoSession.insert(prices);
        final PgeBill bill = new PgeBill();
        bill.setPgePrices(prices);
        daoSession.insert(bill);

        // load
        assertTrue(bill.getId() > 0);
        final PgeBill billFromDb = daoSession.getPgeBillDao().load(bill.getId());
        
        // test
        assertNotNull(billFromDb.getPgePrices());
        assertTrue(billFromDb.getPricesId() > 0);
        assertTrue(billFromDb.getPgePrices().getId() > 0);
        assertSame(prices, billFromDb.getPgePrices());
    }

    public void testLoadAll() {
        // save
        final PgePrices prices = new PgePrices();
        daoSession.insert(prices);
        final PgeG12Bill bill = new PgeG12Bill();
        bill.setPgePrices(prices);
        daoSession.insert(bill);

        // load
        final List<PgeG12Bill> billsFromDb = daoSession.getPgeG12BillDao().loadAll();
        
        // test
        assertEquals(1, billsFromDb.size());
        assertNotNull(billsFromDb.get(0));
        assertNotNull(billsFromDb.get(0).getPgePrices());
    }

    public void testLazyList() {
        // save
        final PgnigPrices prices = new PgnigPrices();
        daoSession.insert(prices);
        final PgnigBill bill = new PgnigBill();
        bill.setPgnigPrices(prices);
        daoSession.insert(bill);

        // load
        final LazyList<PgnigBill> billsFromDb = daoSession.getPgnigBillDao().queryBuilder().listLazy();
        
        // test
        assertEquals(1, billsFromDb.size());
        assertNotNull(billsFromDb.get(0));
        assertNotNull(billsFromDb.get(0).getPgnigPrices());
        assertTrue(billsFromDb.isClosed());
    }
    
    public void testSaveBillTrigger() {
        //TODO
        assertTrue(true);
    }
}
