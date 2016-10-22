package pl.srw.billcalculator.bill.calculation;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class CalculatedEnergyBillTest {

    private int consumption;

    private CalculatedEnergyBill sut = new CalculatedEnergyBill("01/01/2000", "01/01/2000", "0.00", "0.00", "0.00") {
        @Override
        public int getTotalConsumption() {
            return consumption;
        }
    };

    @Test
    @Parameters({
            "01/07/2016,20/07/2016,0|0",
            "01/07/2016,17/07/2016,100|100",
            "01/07/2015,31/07/2015,100|0",
            "01/06/2016,31/07/2016,100|50",
            "01/06/2016,29/08/2016,100|66",
            "01/05/2016,31/08/2016,100|50",
            "01/01/2016,31/12/2016,100|50",
    })
    public void countConsumptionPartFromJuly16(String dateFrom, String dateTo, int consumption, int expected) throws Exception {
        // WHEN
        final int result = sut.countConsumptionPartFromJuly16(dateFrom, dateTo, consumption);

        // THEN
        assertEquals(expected, result);
    }

    @Test
    public void getExcise() throws Exception {
        // GIVEN
        consumption = 100;

        // WHEN
        assertEquals(new BigDecimal("2.00"), sut.getExcise());
    }
}