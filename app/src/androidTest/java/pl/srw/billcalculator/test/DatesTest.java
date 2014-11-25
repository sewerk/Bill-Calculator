package pl.srw.billcalculator.test;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class DatesTest extends TestCase {

    public static void testParse() {
        Date date = Dates.parse("31/12/2014");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        assertEquals(31, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.DECEMBER, cal.get(Calendar.MONTH));
        assertEquals(2014, cal.get(Calendar.YEAR));
    }

    public static void testFormat() {
        String date = Dates.format(2014, Calendar.DECEMBER, 31);

        assertEquals("31/12/2014", date);
    }

    public static void testCountMonth() {
        assertEquals(1, Dates.countMonth("01/01/2000", "31/01/2000"));
        assertEquals(1, Dates.countMonth("01/01/2014", "01/02/2014"));
        assertEquals(2, Dates.countMonth("01/01/2015", "28/02/2015"));
        assertEquals(2, Dates.countMonth("17/03/2014", "15/05/2014"));
        assertEquals(1, Dates.countMonth("02/12/2014", "01/01/2015"));
    }
}
