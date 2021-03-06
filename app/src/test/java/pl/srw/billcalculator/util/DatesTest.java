package pl.srw.billcalculator.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class DatesTest {

    @Test
    public void shouldParseString() {
        LocalDate result = Dates.parse("31/12/2014", Dates.DEFAULT_DATE_PATTERN);

        assertEquals(31, result.getDayOfMonth());
        assertEquals(Month.DECEMBER, result.getMonth());
        assertEquals(12, result.getMonthValue());
        assertEquals(2014, result.getYear());
    }

    @Test
    public void shouldFormatDateValues() {
        String result = Dates.format(2014, Month.DECEMBER, 31, Dates.DEFAULT_DATE_PATTERN);

        assertEquals("31/12/2014", result);
    }

    @Test
    public void shouldFormatDate() {
        final String result = Dates.format(LocalDate.of(2015, Month.FEBRUARY, 22), Dates.DEFAULT_DATE_PATTERN);

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
    @Parameters({"01/01/2000,31/01/2000|1",
            "01/01/2005,16/01/2005|1",
            "01/01/2014,01/02/2014|1",
            "01/01/2015,28/02/2015|2",
            "31/01/2015,28/02/2015|1",
            "30/04/2015,31/05/2015|1",
            "31/01/2016,31/03/2016|2",
            "01/02/2015,14/02/2015|1",
            "01/02/2016,14/02/2016|0",
            "17/03/2014,15/05/2014|2",
            "26/09/2016,27/11/2016|2",
            "02/12/2014,01/01/2015|1",
            "31/07/2015,24/11/2015|4",
            "02/01/2014,30/06/2014|6",
            "11/11/2014,11/11/2015|12"
            })
    public void shouldCountWholeMonth(String fromDate, String toDate, int count) {
        assertEquals(count, Dates.countWholeMonth(parse(fromDate), parse(toDate)));
    }

    @Test
    @Parameters({"01/01/2000,31/01/2000|1.000",
            "01/01/2005,16/01/2005|0.5161",
            "01/01/2014,01/02/2014|1",
            "01/01/2015,28/02/2015|2",
            "31/01/2015,28/02/2015|1",
            "30/04/2015,31/05/2015|1",
            "31/05/2015,30/06/2015|1",
            "31/01/2016,31/03/2016|2",
            "01/02/2015,14/02/2015|0.5",
            "17/03/2014,15/05/2014|1.9032",
            "26/09/2016,27/11/2016|2.0333",
            "02/12/2014,01/01/2015|0.9677",
            "31/07/2015,24/11/2015|3.8",
            "01/01/2014,30/06/2014|6.0",
            "11/11/2014,11/11/2015|12"
    })
    public void shouldCountMonth(String fromDate, String toDate, double count) {
        final double result = Dates.countMonth(parse(fromDate), parse(toDate)).doubleValue();
        assertEquals(count, result, 0.0001);
    }

    @Test
    @Parameters({
            "01/02/2015,31/06/2016|0",
            "01/06/2016,02/07/2016|1",
            "01/06/2016,31/07/2016|30",
            "01/07/2016,31/07/2016|30",
            "01/08/2016,31/09/2016|60",
            "01/08/2016,31/09/2017|425",
    })
    public void shouldCountDaysFromJuly16(String fromDate, String toDate, int count) throws Exception {
        // WHEN
        final int result = Dates.countDaysFromJuly16(parse(fromDate), parse(toDate));

        // THEN
        assertEquals(count, result);
    }

    @Test
    @Parameters({
            "01/05/2015,01/05/2015|0",
            "01/05/2014,01/06/2015|396",
            "01/06/2016,01/07/2016|30",
            "01/06/2016,31/07/2016|60",
            "01/08/2016,30/09/2016|60",
    })
    public void shouldCountDays(String fromDate, String toDate, int count) throws Exception {
        // WHEN
        final int result = Dates.countDays(parse(fromDate), parse(toDate));

        // THEN
        assertEquals(count, result);
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

        final Date storedDate = Dates.toDate(Dates.parse(dateFromUser, Dates.DEFAULT_DATE_PATTERN));
        final LocalDate retrieveFromDatabase = Dates.toLocalDate(storedDate);

        final String dateBackToUser = Dates.format(retrieveFromDatabase, Dates.DEFAULT_DATE_PATTERN);

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

    private LocalDate parse(String text) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}