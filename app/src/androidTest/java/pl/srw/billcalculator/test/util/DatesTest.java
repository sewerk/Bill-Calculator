package pl.srw.billcalculator.test.util;

import junit.framework.TestCase;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.Calendar;
import java.util.Date;

import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class DatesTest extends TestCase {

    public void testParse() {
        LocalDate result = Dates.parse("31/12/2014");

        assertEquals(31, result.getDayOfMonth());
        assertEquals(Month.DECEMBER, result.getMonth());
        assertEquals(12, result.getMonthValue());
        assertEquals(2014, result.getYear());
    }

    public void testFormatDateValues() {
        String result = Dates.format(2014, Month.DECEMBER, 31);

        assertEquals("31/12/2014", result);
    }
    
    public void testFormatDate() {
        final String result = Dates.format(LocalDate.of(2015, Month.FEBRUARY, 22));

        assertEquals("22/02/2015", result);
    }
    
    public void testFirstDayOfMonth() {
        final Calendar expected = Calendar.getInstance();
        
        final LocalDate result = Dates.firstDayOfThisMonth();

        assertEquals(1, result.getDayOfMonth());
        assertEquals(expected.get(Calendar.MONTH) + 1, result.getMonthValue());
        assertEquals(expected.get(Calendar.YEAR), result.getYear());
    }
    
    public void testLastDayOfMonth() {
        final Calendar expected = Calendar.getInstance();
        expected.set(Calendar.DAY_OF_MONTH, 1);
        expected.add(Calendar.MONTH, 1);
        expected.add(Calendar.DAY_OF_MONTH, -1);

        final LocalDate result = Dates.lastDayOfThisMonth();
        
        assertEquals(expected.get(Calendar.DAY_OF_MONTH), result.getDayOfMonth());
        assertEquals(expected.get(Calendar.MONTH) + 1, result.getMonthValue());
        assertEquals(expected.get(Calendar.YEAR), result.getYear());
    }

    public void testCountMonth() {
        assertEquals(1, Dates.countMonth("01/01/2000", "31/01/2000"));
        assertEquals(1, Dates.countMonth("01/01/2005", "16/01/2005"));
        assertEquals(1, Dates.countMonth("01/01/2014", "01/02/2014"));
        assertEquals(2, Dates.countMonth("01/01/2015", "28/02/2015"));
        assertEquals(2, Dates.countMonth("17/03/2014", "15/05/2014"));
        assertEquals(1, Dates.countMonth("02/12/2014", "01/01/2015"));
        assertEquals(6, Dates.countMonth("02/01/2014", "30/06/2014"));
        assertEquals(12, Dates.countMonth("11/11/2014", "11/11/2015"));
    }
}
