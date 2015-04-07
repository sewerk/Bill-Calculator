package pl.srw.billcalculator.util;

import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by kseweryn on 07.04.15.
 */
public class DatesTest {

    @Test
    public void shouldParseString() {
        LocalDate result = Dates.parse("31/12/2014");

        assertEquals(31, result.getDayOfMonth());
        assertEquals(Month.DECEMBER, result.getMonth());
        assertEquals(12, result.getMonthValue());
        assertEquals(2014, result.getYear());
    }

    @Test
    public void shouldFormatDateValues() {
        String result = Dates.format(2014, Month.DECEMBER, 31);

        assertEquals("31/12/2014", result);
    }

    @Test
    public void shouldFormatDate() {
        final String result = Dates.format(LocalDate.of(2015, Month.FEBRUARY, 22));

        assertEquals("22/02/2015", result);
    }

    @Test
    public void shouldReturnFirstDayOfMonth() {
        final Calendar expected = Calendar.getInstance();

        final LocalDate result = Dates.firstDayOfThisMonth();

        assertEquals(1, result.getDayOfMonth());
        assertEquals(expected.get(Calendar.MONTH) + 1, result.getMonthValue());
        assertEquals(expected.get(Calendar.YEAR), result.getYear());
    }

    @Test
    public void shouldReturnLastDayOfMonth() {
        final Calendar expected = Calendar.getInstance();
        expected.set(Calendar.DAY_OF_MONTH, 1);
        expected.add(Calendar.MONTH, 1);
        expected.add(Calendar.DAY_OF_MONTH, -1);

        final LocalDate result = Dates.lastDayOfThisMonth();

        assertEquals(expected.get(Calendar.DAY_OF_MONTH), result.getDayOfMonth());
        assertEquals(expected.get(Calendar.MONTH) + 1, result.getMonthValue());
        assertEquals(expected.get(Calendar.YEAR), result.getYear());
    }

    @Test
    public void countMonth() {
        assertEquals(1, Dates.countMonth("01/01/2000", "31/01/2000"));
        assertEquals(1, Dates.countMonth("01/01/2005", "16/01/2005"));
        assertEquals(1, Dates.countMonth("01/01/2014", "01/02/2014"));
        assertEquals(2, Dates.countMonth("01/01/2015", "28/02/2015"));
        assertEquals(2, Dates.countMonth("17/03/2014", "15/05/2014"));
        assertEquals(1, Dates.countMonth("02/12/2014", "01/01/2015"));
        assertEquals(6, Dates.countMonth("02/01/2014", "30/06/2014"));
        assertEquals(12, Dates.countMonth("11/11/2014", "11/11/2015"));
    }

    @Test
    public void shouldConvertToLocalDate() {
        assertEquals(LocalDate.of(1970, Month.JANUARY, 1), Dates.toLocalDate(new Date(0)));

        final Calendar c = Calendar.getInstance();
        c.set(2015, Calendar.JANUARY, 23);
        assertEquals(LocalDate.of(2015, Month.JANUARY, 23), Dates.toLocalDate(c.getTime()));

        final Date utilDate = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        assertEquals(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)),
                Dates.toLocalDate(utilDate));
    }

    @Test
    public void wholeConversionCycle() {
        final String dateFromUser = "23/02/2015";

        final Date storedDate = Dates.toDate(Dates.parse(dateFromUser));
        final LocalDate retrieveFromDatabase = Dates.toLocalDate(storedDate);

        final String dateBackToUser = Dates.format(retrieveFromDatabase);

        assertEquals(dateFromUser, dateBackToUser);
    }

    @Test
    public void shouldConvertToUtilDate() {
        assertEqualsDates(new Date(0), Dates.toDate(LocalDate.of(1970, Month.JANUARY, 1)));

        final Calendar c = Calendar.getInstance();
        c.set(2015, Calendar.JANUARY, 23);
        assertEqualsDates(c.getTime(), Dates.toDate(LocalDate.of(2015, Month.JANUARY, 23)));

        final Date utilDate = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        final LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        assertEqualsDates(utilDate, Dates.toDate(localDate));
    }

    private void assertEqualsDates(Date date, Date date1) {
        assertEquals(format(date), format(date1));
    }

    private String format(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date);
    }
}