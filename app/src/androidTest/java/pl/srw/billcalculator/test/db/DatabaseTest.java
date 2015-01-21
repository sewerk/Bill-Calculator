package pl.srw.billcalculator.test.db;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import de.greenrobot.dao.test.AbstractDaoSessionTest;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.EnergyBillActivity;
import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgeG12Prices;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;

/**
 * Created by Kamil Seweryn.
 */
public class DatabaseTest extends AbstractDaoSessionTest<BillCalculator, DaoMaster, DaoSession> {

    public DatabaseTest() {
        super(DaoMaster.class);
    }

    public void testBillSavedWithPrices() {
        final PgePrices prices = new PgePrices();
        daoSession.insert(prices);

        final PgeBill bill = new PgeBill();
        bill.setPgePrices(prices);
        daoSession.insert(bill);

        assertTrue(bill.getId() > 0);
        final PgeBill billFromDb = daoSession.getPgeBillDao().load(bill.getId());
        assertNotNull(billFromDb.getPgePrices());
        assertTrue(billFromDb.getPricesId() > 0);
        assertTrue(billFromDb.getPgePrices().getId() > 0);
        assertSame(prices, billFromDb.getPgePrices());
    }

    public void testBillListRetrievedWithPrices() {
        final PgeG12Prices prices = new PgeG12Prices();
        daoSession.insert(prices);

        final PgeG12Bill bill = new PgeG12Bill();
        bill.setPgeG12Prices(prices);
        daoSession.insert(bill);

        final List<PgeG12Bill> billsFromDb = daoSession.getPgeG12BillDao().loadAll();
        assertEquals(1, billsFromDb.size());
        assertNotNull(billsFromDb.get(0));
        assertNotNull(billsFromDb.get(0).getPgeG12Prices());
    }
}
