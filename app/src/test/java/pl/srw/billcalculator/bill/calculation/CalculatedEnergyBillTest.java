package pl.srw.billcalculator.bill.calculation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.math.BigDecimal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pl.srw.billcalculator.util.Dates;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class CalculatedEnergyBillTest {

    private int consumption;

    private CalculatedEnergyBill sut = new CalculatedEnergyBill(LocalDate.of(2000, Month.JANUARY, 1), LocalDate.of(2000, Month.JANUARY, 1), "0.00", "0.00", "0.00") {
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
        final int result = sut.countConsumptionPartFromJuly16(
                Dates.parse(dateFrom, Dates.DEFAULT_DATE_PATTERN),
                Dates.parse(dateTo, Dates.DEFAULT_DATE_PATTERN),
                consumption);

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