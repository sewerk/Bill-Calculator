package pl.srw.billcalculator.db;

import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.test.AbstractDaoSessionTest;

import java.util.List;

import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;

public class DatabaseTest extends AbstractDaoSessionTest<DaoMaster, DaoSession> {

    public DatabaseTest() {
        super(DaoMaster.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public void testLoadById() {
        // save
        final PgePrices prices = new PgePrices();
        daoSession.insert(prices);
        final PgeG11Bill bill = new PgeG11Bill();
        bill.setPgePrices(prices);
        daoSession.insert(bill);

        // load
        assertTrue(bill.getId() > 0);
        final PgeG11Bill billFromDb = daoSession.getPgeG11BillDao().load(bill.getId());
        
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
    
    public void testDelete() {
        // save
        final PgePrices prices = new PgePrices();
        daoSession.insert(prices);
        final PgeG11Bill bill = new PgeG11Bill();
        bill.setPgePrices(prices);
        daoSession.insert(bill);

        // delete
        daoSession.delete(bill);
        daoSession.delete(prices);
        
        // test
        assertTrue(daoSession.getPgeG11BillDao().loadAll().isEmpty());
        assertTrue(daoSession.getPgePricesDao().loadAll().isEmpty());
    }
}
